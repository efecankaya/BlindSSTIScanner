package sstiscanner.utils;

import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;

import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;

public class ScanIssue {

    public static AuditIssue generateIssue(ExecutedAttack executedAttack) {
        return auditIssue(
                "Out of Band Code Code Execution via SSTI (" + executedAttack.engine().getName() + ")",
                """
                Parameter: <b>%s</b> <br/> Template engine: %s <br/> Payload: %s <br/> Collaborator callback id: %s
                """.formatted(executedAttack.insertionPoint().name(), executedAttack.engine().getName(), executedAttack.payload(), executedAttack.id()),
                null,
                executedAttack.baseRequestResponse().request().url(),
                AuditIssueSeverity.HIGH,
                AuditIssueConfidence.CERTAIN,
                null,
                null,
                AuditIssueSeverity.HIGH,
                executedAttack.attackRequestResponse().withResponseMarkers()
        );
    }
}
