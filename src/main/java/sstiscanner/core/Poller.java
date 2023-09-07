package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.scanner.audit.issues.AuditIssue;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Poller {
    private final CollaboratorClient collaboratorClient;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ScheduledFuture<?> schedule;
    private final Attacks attacks;
    private final MontoyaApi api;
    private final InteractionHandler interactionHandler;

    public Poller(CollaboratorClient collaboratorClient, Attacks attacks, MontoyaApi api, InteractionHandler interactionHandler) {
        this.collaboratorClient = collaboratorClient;
        this.attacks = attacks;
        this.api = api;
        this.interactionHandler = interactionHandler;
    }

    public void start(Duration duration) {
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        this.schedule = this.scheduledThreadPoolExecutor.scheduleAtFixedRate(new PollingRunnable(), 0, duration.getSeconds(), TimeUnit.SECONDS);
    }

    public void update(Duration duration) {
        this.shutdown();
        this.start(duration);
    }

    public void shutdown() {
        this.schedule.cancel(true);
        this.scheduledThreadPoolExecutor.shutdownNow();
    }

    public void poll() {
        interactionHandler.handleInteractions(this.attacks.getAllAttacks(), this.collaboratorClient.getAllInteractions());
    }

    private class PollingRunnable implements Runnable {
        @Override
        public void run() {
            poll();
        }
    }
}
