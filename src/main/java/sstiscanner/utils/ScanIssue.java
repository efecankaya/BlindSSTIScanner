package sstiscanner.utils;

import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import sstiscanner.engines.Engine;

import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;

public class ScanIssue {

    public static AuditIssue generateIssue(AuditInsertionPoint insertionPoint, ExecutedAttack executedAttack, Engine engine) {
        return auditIssue(
                "Code Execution via SSTI (" + engine.name + ")",
                """
                Parameter: <b>%s</b> <br/> Template engine: %s <br/> Payload: %s <br/> Collaborator callback id: %s
                """.formatted(insertionPoint.name(), engine.name, executedAttack.payload(), executedAttack.id()),
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
