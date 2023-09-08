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
import sstiscanner.engines.Contexts;
import sstiscanner.engines.Engine;
import sstiscanner.engines.Engines;
import sstiscanner.utils.ExecutedAttack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static burp.api.montoya.core.ByteArray.byteArray;

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

    public List<AuditIssue> attack(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint) {
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
                    List<String> payloads = new ArrayList<>(List.of(engine.getPayload()));

                    if (this.config.isContextEscapeEnabled()) {
                        payloads.addAll(Contexts.generateCodeEscapePayloads(engine.getPayload(), engine.getContexts()));
                    }

                    this.api.logging().logToOutput("Testing for engine " + engine.getName());

                    for (String payload : payloads) {
                        CollaboratorPayload collaboratorPayload = this.collaboratorClient.generatePayload();
                        String collaboratorURL = collaboratorPayload.toString();

                        String command = this.config.getCommand().replace("<COLLABORATOR>", collaboratorURL);
                        String currentPayload = payload.replace("[COMMAND]", command);

                        currentAttacks.add(attackWithPayload(baseRequestResponse, auditInsertionPoint, currentPayload, collaboratorPayload, engine));

                        List<Interaction> interactions = this.collaboratorClient.getAllInteractions();
                        if (interactions.size() > 0) {
                            return interactionHandler.generateIssues(currentAttacks, interactions);
                        }
                    }

                    if (this.config.isKeepTrackEnabled()) {
                        this.attacks.addAll(currentAttacks);
                    }

                    currentAttacks.clear();
                }
            }
        }

        return new ArrayList<>();
    }

    private ExecutedAttack attackWithPayload(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint, String payload, CollaboratorPayload collaboratorPayload, Engine engine) {
        HttpRequest attackRequest = auditInsertionPoint.buildHttpRequestWithPayload(byteArray(payload)).withService(baseRequestResponse.httpService());
        HttpRequestResponse attackRequestResponse = this.api.http().sendRequest(attackRequest);
        return new ExecutedAttack(collaboratorPayload.id().toString(), payload, engine, auditInsertionPoint, baseRequestResponse, attackRequestResponse);
    }
}
