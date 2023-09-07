package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.sitemap.SiteMapFilter;
import sstiscanner.utils.ExecutedAttack;
import sstiscanner.utils.ScanIssue;

import java.util.List;
import java.util.stream.Stream;

public class InteractionHandler {

    private final MontoyaApi api;

    public InteractionHandler(MontoyaApi api) {
        this.api = api;
    }

    private static boolean isInteracted(ExecutedAttack executedAttack, List<Interaction> interactions) {
        return interactions.stream().anyMatch(interaction -> executedAttack.id().equals(interaction.id().toString()));
    }

    public List<AuditIssue> generateIssues(List<ExecutedAttack> attacks, List<Interaction> interactions) {
        Stream<ExecutedAttack> successfulAttacks = attacks.stream().filter(executedAttack -> isInteracted(executedAttack, interactions));
        return successfulAttacks.map(ScanIssue::generateIssue).toList();
    }

    public void handleInteractions(List<ExecutedAttack> attacks, List<Interaction> interactions) {
        List<AuditIssue> issues = this.generateIssues(attacks, interactions);
        this.addToSiteMap(issues);
    }

    private void addToSiteMap(List<AuditIssue> issues) {
        for (AuditIssue newIssue : issues) {
            /*
            boolean issueExists = false;
            for (AuditIssue existingIssue : this.api.siteMap().issues()) {
                if (existingIssue.name().equals(newIssue.name())) {
                    issueExists = true;
                    break;
                }
            }
            if (!issueExists) this.api.siteMap().add(newIssue);
            */
            this.api.siteMap().add(newIssue);
        }
    }
}
