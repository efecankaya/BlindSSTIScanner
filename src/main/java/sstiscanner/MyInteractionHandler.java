package sstiscanner;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import sstiscanner.poller.InteractionHandler;

import java.util.List;

public class MyInteractionHandler implements InteractionHandler {
    private final MontoyaApi api;
    private final InteractionLogger interactionLogger;

    public MyInteractionHandler(MontoyaApi api, InteractionLogger interactionLogger) {
        this.api = api;
        this.interactionLogger = interactionLogger;
    }

    @Override
    public void handleInteraction(Interaction interaction) {
        interactionLogger.logInteraction(interaction);

        List<ProxyHttpRequestResponse> proxyHttpRequestResponseList = api.proxy().history(requestResponse -> requestResponse.finalRequest().toString().contains(interaction.id().toString()));

        proxyHttpRequestResponseList.forEach(item -> api.siteMap().add(
                AuditIssue.auditIssue(
                        "Collaborator example issue",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                        item.finalRequest().url(),
                        AuditIssueSeverity.HIGH,
                        AuditIssueConfidence.CERTAIN,
                        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        AuditIssueSeverity.HIGH,
                        HttpRequestResponse.httpRequestResponse(item.finalRequest(), item.originalResponse())
                )
        ));
    }
}