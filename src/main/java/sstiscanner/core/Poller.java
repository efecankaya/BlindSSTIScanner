package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.Interaction;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Poller {
    private final CollaboratorClient collaboratorClient;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ScheduledFuture<?> schedule;
    MontoyaApi api;

    public Poller(CollaboratorClient collaboratorClient, MontoyaApi api) {
        this.collaboratorClient = collaboratorClient;
        this.api = api;
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
        List<Interaction> interactionList = collaboratorClient.getAllInteractions();
        this.api.logging().logToOutput("get all interactions...");

        for (Interaction interaction : interactionList) {
            this.api.logging().logToOutput(interaction.toString());
        }
    }

    private class PollingRunnable implements Runnable {
        @Override
        public void run() {
            poll();
        }
    }
}
