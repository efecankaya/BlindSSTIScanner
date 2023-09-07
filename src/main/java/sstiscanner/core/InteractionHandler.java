package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import sstiscanner.utils.AttackInteraction;
import sstiscanner.utils.ExecutedAttack;
import sstiscanner.utils.ScanIssue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InteractionHandler {

    private final MontoyaApi api;

    public InteractionHandler(MontoyaApi api) {
        this.api = api;
    }

    public List<AuditIssue> generateIssues(List<ExecutedAttack> attacks, List<Interaction> interactions) {
        return attacks.stream()
                .flatMap(attack -> interactions.stream()
                        .filter(interaction -> attack.id().equals(interaction.id().toString()))
                        .map(interaction -> new AttackInteraction(attack, interaction)))
                .map(ScanIssue::generateIssue)
                .collect(Collectors.toList());
    }

    public void handleInteractions(List<ExecutedAttack> attacks, List<Interaction> interactions) {
        this.addToSiteMap(this.generateIssues(attacks, interactions));
    }

    private void addToSiteMap(List<AuditIssue> issues) {
        issues.forEach(newIssue -> this.api.siteMap().add(newIssue));
    }
}
