package sstiscanner.utils;

import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueDefinition;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;

import java.util.List;

import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;

public class ScanIssue {

    public static AuditIssue generateIssue(AttackInteraction attackInteraction) {
        return auditIssue(
                "Out of Band Code Execution via SSTI (" + attackInteraction.attack().engine().getName() + ")",
                """
                The Collaborator server received a %s interaction from IP address %s at %s<br>
                <br>
                The insertion point %s appears to be vulnerable to out-of-band code execution via server-side template injection attacks. The template engine appears to be <b>%s</b>.<br>
                <br>
                The payload <code>%s</code> was submitted in the insertion point %s, which generated a callback with the id of <code>%s</code>.<br>
                """.formatted(
                        attackInteraction.interaction().type(),
                        attackInteraction.interaction().clientIp().getHostAddress(),
                        attackInteraction.interaction().timeStamp(),
                        attackInteraction.attack().insertionPoint().name(),
                        attackInteraction.attack().engine().getName(),
                        attackInteraction.attack().payload(),
                        attackInteraction.attack().insertionPoint().name(),
                        attackInteraction.interaction().id()
                ),
                null,
                attackInteraction.attack().attackRequestResponse().request().url(),
                AuditIssueSeverity.HIGH,
                AuditIssueConfidence.CERTAIN,
                null,
                null,
                AuditIssueSeverity.HIGH,
                attackInteraction.attack().attackRequestResponse().withResponseMarkers()
        );
    }
}
