package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.CollaboratorPayload;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.http.Http;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import sstiscanner.engines.Engine;
import sstiscanner.engines.Engines;
import sstiscanner.utils.Config;
import sstiscanner.utils.ExecutedAttack;
import sstiscanner.utils.ScanIssue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static burp.api.montoya.core.ByteArray.byteArray;
import static java.lang.String.format;

public class Attacker {

    private final MontoyaApi api;
    private Logging logger;
    private Http http;
    private CollaboratorClient collaboratorClient;
    private Engines engines;
    private Config config;
    public String cmd = "curl";

    public Attacker(MontoyaApi api, Engines engines, Config config) {
        this.api = api;
        this.logger = this.api.logging();
        this.http = this.api.http();
        this.collaboratorClient = this.api.collaborator().createClient();
        this.engines = engines;
        this.config = config;
    }

    public List<AuditIssue> blindAttack(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint) {
        List<ExecutedAttack> executedAttacks = new ArrayList<>();

        for (Engine engine : engines.getEngines()) {
            if(config.isEngineEnabled(engine.getName())) {
                CollaboratorPayload collaboratorPayload = this.collaboratorClient.generatePayload();
                String collaboratorURL = collaboratorPayload.toString();
                this.logger.logToOutput("Generated collaborator URL: " + collaboratorURL);
                this.logger.logToOutput("Engine Name: " + engine.getName());
                String command = this.cmd + " " + engine.getName() + "." + collaboratorURL;
                String payload = engine.getPayload().replace("[COMMAND]", command);

                this.logger.logToOutput("Sending payload: " + payload + " to insertion point " + auditInsertionPoint.name());
                executedAttacks.add(attackWithPayload(baseRequestResponse, auditInsertionPoint, payload, collaboratorPayload, engine));
            }
        }

        List<Interaction> interactions = this.collaboratorClient.getAllInteractions();
        this.logger.logToOutput("Number of interactions: " + interactions.size());
        for (Interaction interaction : interactions) {
            this.logger.logToOutput(format("""
                    Interaction type: %s
                    Interaction ID: %s
                    Interaction details: %s
                    """, interaction.type().name(), interaction.id(), interaction.httpDetails()));
        }

        Stream<ExecutedAttack> successfulAttacks = executedAttacks.stream().filter(executedAttack -> isInteracted(interactions, executedAttack));

        return successfulAttacks
                .map(executedAttack -> {
                    Engine engine = executedAttack.engine();
                    return ScanIssue.generateIssue(auditInsertionPoint, executedAttack, engine);
                })
                .toList();
    }

    private ExecutedAttack attackWithPayload(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint, String payload, CollaboratorPayload collaboratorPayload, Engine engine) {
        HttpRequest attackRequest = auditInsertionPoint.buildHttpRequestWithPayload(byteArray(payload)).withService(baseRequestResponse.httpService());
        List<ExecutedAttack> executedAttacks = new ArrayList<>();
        HttpRequestResponse attackRequestResponse = this.http.sendRequest(attackRequest);
        this.logger.logToOutput("Sent attack request, got response: " + attackRequestResponse.response().statusCode());
        return new ExecutedAttack(collaboratorPayload.id().toString(), payload, engine, auditInsertionPoint, baseRequestResponse, attackRequestResponse);
    }

    private boolean isInteracted(List<Interaction> interactions, ExecutedAttack executedAttack) {
        String id = executedAttack.id();
        return interactions.stream().anyMatch(interaction -> id.equals(interaction.id().toString()));
    }
}
