package sstiscanner;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.SecretKey;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.persistence.PersistedObject;
import sstiscanner.poller.Poller;

import java.time.Duration;

public class Collaborator {
    private final MontoyaApi api;
    private final Logging logger;
    public CollaboratorClient collaboratorClient;

    public Collaborator(MontoyaApi api) {
        this.api = api;
        this.logger = this.api.logging();

        this.collaboratorClient = createCollaboratorClient(this.api.persistence().extensionData());

        // Log any stored interactions.
        InteractionLogger interactionLogger = new InteractionLogger(this.api);
        interactionLogger.logInteractions(this.collaboratorClient.getAllInteractions());

        // Periodically poll the CollaboratorClient to retrieve any new interactions.
        Poller collaboratorPoller = new Poller(this.logger, this.collaboratorClient, Duration.ofSeconds(10));
        collaboratorPoller.registerInteractionHandler(new MyInteractionHandler(api, interactionLogger));
        collaboratorPoller.start();

        api.extension().registerUnloadingHandler(() -> {
            // Stop polling the CollaboratorClient.
            collaboratorPoller.shutdown();

            this.logger.logToOutput("Extension unloading...");
        });
    }


    private CollaboratorClient createCollaboratorClient(PersistedObject persistedData) {
        CollaboratorClient collaboratorClient;

        String existingCollaboratorKey = persistedData.getString("persisted_collaborator");

        if (existingCollaboratorKey != null) {
            this.logger.logToOutput("Creating Collaborator client from key.");
            collaboratorClient = api.collaborator().restoreClient(SecretKey.secretKey(existingCollaboratorKey));
        } else {
            this.logger.logToOutput("No previously found Collaborator client. Creating new client...");
            collaboratorClient = api.collaborator().createClient();

            // Save the secret key of the CollaboratorClient so that you can retrieve it later.
            this.logger.logToOutput("Saving Collaborator secret key.");
            persistedData.setString("persisted_collaborator", collaboratorClient.getSecretKey().toString());
        }

        return collaboratorClient;
    }
}
