package sstiscanner.poller;

import burp.api.montoya.collaborator.CollaboratorClient;
import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.logging.Logging;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Poller {
    private final CollaboratorClient collaboratorClient;
    private final Duration pollInterval;
    private final List<InteractionHandler> interactionHandlers;
    private final Logging logger;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ScheduledFuture<?> schedule;

    public Poller(Logging logger, CollaboratorClient collaboratorClient, Duration pollInterval) {
        this.collaboratorClient = collaboratorClient;
        this.pollInterval = pollInterval;
        this.interactionHandlers = new LinkedList<>();
        this.logger = logger;
    }

    public void registerInteractionHandler(InteractionHandler interactionHandler) {
        interactionHandlers.add(interactionHandler);
    }

    public void start() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        schedule = scheduledThreadPoolExecutor.scheduleAtFixedRate(new PollingRunnable(), 0, pollInterval.getSeconds(), TimeUnit.SECONDS);
    }

    public void shutdown() {
        schedule.cancel(true);
        scheduledThreadPoolExecutor.shutdown();
    }

    private class PollingRunnable implements Runnable {
        @Override
        public void run() {
            logger.logToOutput("Polling...");
            List<Interaction> interactionList = collaboratorClient.getAllInteractions();

            for (Interaction interaction : interactionList) {
                for (InteractionHandler interactionHandler : interactionHandlers) {
                    interactionHandler.handleInteraction(interaction);
                }
            }
        }
    }
}