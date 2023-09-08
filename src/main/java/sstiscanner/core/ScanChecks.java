package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.AuditResult;
import burp.api.montoya.scanner.ConsolidationAction;
import burp.api.montoya.scanner.ScanCheck;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;

import java.util.ArrayList;

import static burp.api.montoya.scanner.AuditResult.auditResult;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_BOTH;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_EXISTING;

public class ScanChecks implements ScanCheck {

    private final MontoyaApi api;
    private final Attacker attacker;

    public ScanChecks(MontoyaApi api, Attacker attacker)
    {
        this.api = api;
        this.attacker = attacker;
    }

    @Override
    public AuditResult activeAudit(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint) {
        return auditResult(new ArrayList<>(this.attacker.attack(baseRequestResponse, auditInsertionPoint)));
    }

    @Override
    public AuditResult passiveAudit(HttpRequestResponse baseRequestResponse) {
        return AuditResult.auditResult(new ArrayList<>());
    }

    @Override
    public ConsolidationAction consolidateIssues(AuditIssue newIssue, AuditIssue existingIssue) {
        return (existingIssue.name().equals(newIssue.name())) ? KEEP_EXISTING : KEEP_BOTH;
    }
}
