package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.CollaboratorPayload;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.analysis.AttributeType;
import burp.api.montoya.http.message.responses.analysis.ResponseVariationsAnalyzer;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import sstiscanner.engines.Engine;
import sstiscanner.engines.Engines;
import sstiscanner.utils.ExecutedAttack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static burp.api.montoya.core.ByteArray.byteArray;
import static java.lang.String.format;

public class Attacker {

    private final MontoyaApi api;
    private final CollaboratorClient collaboratorClient;
    private final Engines engines;
    private final Config config;
    private final Attacks attacks;
    private final InteractionHandler interactionHandler;

    public Attacker(MontoyaApi api, Engines engines, Config config, CollaboratorClient collaboratorClient, Attacks attacks, InteractionHandler interactionHandler) {
        this.api = api;
        this.collaboratorClient = collaboratorClient;
        this.engines = engines;
        this.config = config;
        this.attacks = attacks;
        this.interactionHandler = interactionHandler;
    }

    public List<AuditIssue> blindAttack(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint) {
        List<ExecutedAttack> currentAttacks = new ArrayList<>();
        boolean isInteresting = false;

        if (this.config.isPolyglotEnabled()) {
            for (String polyglot : this.engines.getPolyglots()) {
                HttpRequest attackRequest = auditInsertionPoint.buildHttpRequestWithPayload(byteArray(polyglot)).withService(baseRequestResponse.httpService());
                HttpRequestResponse attackRequestResponse = this.api.http().sendRequest(attackRequest);

                ResponseVariationsAnalyzer analyzer = this.api.http().createResponseVariationsAnalyzer();
                analyzer.updateWith(baseRequestResponse.response());
                analyzer.updateWith(attackRequestResponse.response());

                Set<AttributeType> variants = analyzer.variantAttributes();
                if (variants.contains(AttributeType.STATUS_CODE) || variants.contains(AttributeType.LINE_COUNT)) {
                    isInteresting = true;
                    break;
                }
            }
        }

        if (!this.config.isPolyglotEnabled() || isInteresting) {
            for (Engine engine : this.engines.getEngines()) {
                if(this.config.isEngineEnabled(engine.getName())) {
                    CollaboratorPayload collaboratorPayload = this.collaboratorClient.generatePayload();
                    String collaboratorURL = collaboratorPayload.toString();
                    this.api.logging().logToOutput("Generated collaborator URL: " + collaboratorURL);
                    this.api.logging().logToOutput("Engine Name: " + engine.getName());
                    String command = this.config.getCommand().replace("<COLLABORATOR>", collaboratorURL);
                    String payload = engine.getPayload().replace("[COMMAND]", command);

                    this.api.logging().logToOutput("Sending payload: " + payload + " to insertion point " + auditInsertionPoint.name());
                    currentAttacks.add(attackWithPayload(baseRequestResponse, auditInsertionPoint, payload, collaboratorPayload, engine));
                }
            }
        }

        if (this.config.isKeepTrackEnabled()) {
            this.attacks.addAll(currentAttacks);
        }

        List<Interaction> interactions = this.collaboratorClient.getAllInteractions();
        this.api.logging().logToOutput("Number of interactions: " + interactions.size());
        for (Interaction interaction : interactions) {
            this.api.logging().logToOutput(format("""
                    Interaction type: %s
                    Interaction ID: %s
                    Interaction details: %s
                    """, interaction.type().name(), interaction.id(), interaction.httpDetails().isPresent() ? interaction.httpDetails().get().requestResponse().request() : "none"));
        }

        return interactionHandler.generateIssues(currentAttacks, interactions);
    }

    private ExecutedAttack attackWithPayload(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint, String payload, CollaboratorPayload collaboratorPayload, Engine engine) {
        HttpRequest attackRequest = auditInsertionPoint.buildHttpRequestWithPayload(byteArray(payload)).withService(baseRequestResponse.httpService());
        HttpRequestResponse attackRequestResponse = this.api.http().sendRequest(attackRequest);
        this.api.logging().logToOutput("Sent attack request, got response: " + attackRequestResponse.response().statusCode());
        return new ExecutedAttack(collaboratorPayload.id().toString(), payload, engine, auditInsertionPoint, baseRequestResponse, attackRequestResponse);
    }


}
