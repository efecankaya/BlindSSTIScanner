package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import sstiscanner.utils.AttackInteraction;
import sstiscanner.utils.ExecutedAttack;
import sstiscanner.utils.ScanIssue;

import java.util.ArrayList;
import java.util.List;

public class InteractionHandler {

    private final MontoyaApi api;

    public InteractionHandler(MontoyaApi api) {
        this.api = api;
    }

    public List<AuditIssue> generateIssues(List<ExecutedAttack> attacks, List<Interaction> interactions) {
        List<AttackInteraction> successfulAttacks = new ArrayList<>();

        for (ExecutedAttack attack : attacks) {
            for (Interaction interaction : interactions) {
                if (attack.id().equals(interaction.id().toString())) {
                    successfulAttacks.add(new AttackInteraction(attack, interaction));
                    break;
                }
            }
        }

        List<AuditIssue> issues = new ArrayList<>();
        for (AttackInteraction attackInteraction : successfulAttacks) {
            issues.add(ScanIssue.generateIssue(attackInteraction));
        }

        return issues;
    }

    public void handleInteractions(List<ExecutedAttack> attacks, List<Interaction> interactions) {
        this.addToSiteMap(this.generateIssues(attacks, interactions));
    }

    private void addToSiteMap(List<AuditIssue> issues) {
        issues.forEach(newIssue -> this.api.siteMap().add(newIssue));
    }
}
