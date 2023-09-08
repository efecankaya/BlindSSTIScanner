package sstiscanner.core;

import burp.api.montoya.MontoyaApi;
import sstiscanner.engines.Engine;
import sstiscanner.engines.Engines;
import sstiscanner.utils.Command;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class Config {
    private boolean polyglotIsEnabled;
    private boolean contextEscapeIsEnabled;
    private boolean keepTrackOfGenerated;
    private final Set<String> enabledEngines;
    private final Engines engines;
    private final Command command;
    private final Poller poller;
    private final Attacks attacks;
    MontoyaApi api;

    public Config(MontoyaApi api, Engines engines, Poller poller, Attacks attacks) {
        this.api = api;
        this.engines = engines;
        this.command = new Command();
        this.poller = poller;
        this.attacks = attacks;

        this.polyglotIsEnabled = false;
        this.contextEscapeIsEnabled = false;
        this.enabledEngines = new HashSet<>();
        this.enableAllEngines();
    }

    public void enableEngine(String engineName) {
        this.enabledEngines.add(engineName);
    }

    public void disableEngine(String engineName) {
        this.enabledEngines.remove(engineName);
    }

    public boolean isEngineEnabled(String engineName) {
        return this.enabledEngines.contains(engineName);
    }

    public void enableAllEngines() {
        for (Engine engine : engines.getEngines()) {
            this.enableEngine(engine.getName());
        }
    }

    public void disableAllEngines() {
        this.enabledEngines.clear();
    }

    public void setPolyglotEnabled(boolean polyglotIsEnabled) {
        this.polyglotIsEnabled = polyglotIsEnabled;
    }

    public boolean isPolyglotEnabled() {
        return this.polyglotIsEnabled;
    }

    public void setContextEscapeEnabled(boolean contextEscapeIsEnabled) {
        this.contextEscapeIsEnabled = contextEscapeIsEnabled;
    }

    public boolean isContextEscapeEnabled() {
        return this.contextEscapeIsEnabled;
    }

    public String getCommand() {
        return this.command.getSelectedCommand();
    }

    public void setCommand(String customCommand) {
        this.command.setCustomCommand(customCommand);
    }

    public void setCommandOption(String option) {
        this.command.setSelectedOption(option);
    }

    public void startPolling(Duration duration) {
        this.poller.start(duration);
    }

    public void updatePollDuration(Duration duration) {
        this.poller.update(duration);
    }

    public void stopPolling() {
        this.poller.shutdown();
    }

    public void pollNow() {
        this.poller.poll();
    }

    public boolean isKeepTrackEnabled() {
        return this.keepTrackOfGenerated;
    }

    public void setKeepTrackEnabled(boolean keepTrackOfGenerated) {
        this.keepTrackOfGenerated = keepTrackOfGenerated;
    }

    public void clearAttacks() {
        this.attacks.clear();
    }
}
