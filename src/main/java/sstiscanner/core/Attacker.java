package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.http.Http;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import sstiscanner.ExecutedAttack;
import sstiscanner.engines.Jinja2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static burp.api.montoya.core.ByteArray.byteArray;
import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;
import static java.lang.String.format;

public class Attacker {

    private final MontoyaApi api;
    private Logging logger;
    private Http http;
    private CollaboratorClient collaboratorClient;

    public Attacker(MontoyaApi api) {
        this.api = api;
        this.logger = this.api.logging();
        this.http = this.api.http();
        this.collaboratorClient = this.api.collaborator().createClient();
    }

    public List<AuditIssue> blindAttack(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint) {
        String collaboratorPayload = this.collaboratorClient.generatePayload().toString();
        this.logger.logToOutput("Generated collaborator payload: " + collaboratorPayload);

        String payload = Jinja2.blind.replace("[PAYLOAD]", collaboratorPayload);
        this.logger.logToOutput("Sending payload: " + payload + " to insertion point " + auditInsertionPoint.name());

        var executedAttacks = attackWithPayload(baseRequestResponse, auditInsertionPoint, payload);

        List<Interaction> interactions = this.collaboratorClient.getAllInteractions();
        this.logger.logToOutput("Number of interactions: " + interactions.size());
        for (Interaction interaction : interactions) {
            this.logger.logToOutput(format("""
                    Interaction type: %s
                    Interaction ID: %s
                    Interaction details: %s
                    """, interaction.type().name(), interaction.id(), interaction.httpDetails()));
        }

        return executedAttacks.stream().filter(executedAttack -> isInteracted(interactions, executedAttack)).map(this::generateIssue).collect(Collectors.toList());
    }

    private AuditIssue generateIssue(ExecutedAttack executedAttack) {
        return auditIssue("SSTI", "Payload: " + executedAttack.payload(), null, executedAttack.originalRequestResponse().request().url(), AuditIssueSeverity.HIGH, AuditIssueConfidence.CERTAIN, null, null, AuditIssueSeverity.HIGH, executedAttack.attackRequestResponse().withResponseMarkers());
    }

    private List<ExecutedAttack> attackWithPayload(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint, String payload) {
        HttpRequest checkRequest = auditInsertionPoint.buildHttpRequestWithPayload(byteArray(payload)).withService(baseRequestResponse.httpService());
        List<ExecutedAttack> executedAttacks = new ArrayList<>();
        HttpRequestResponse attackRequestResponse = this.http.sendRequest(checkRequest);
        this.logger.logToOutput("Sent request, got response: " + attackRequestResponse.statusCode());
        executedAttacks.add(new ExecutedAttack(payload, baseRequestResponse, attackRequestResponse));
        return executedAttacks;
    }

    private boolean isInteracted(List<Interaction> interactions, ExecutedAttack executedAttack) {
        String payload = executedAttack.payload();
        //return interactions.stream().anyMatch(interaction -> payload.startsWith(interaction.id().toString()));
        return true;
    }
}
