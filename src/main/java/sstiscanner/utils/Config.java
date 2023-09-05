package sstiscanner.utils;

import sstiscanner.engines.Engine;
import sstiscanner.engines.Engines;

import java.util.HashSet;
import java.util.Set;

public class Config {
    private boolean polyglotIsEnabled;
    private boolean contextEscapeIsEnabled;
    private boolean pollingIsEnabled;
    private final Set<String> enabledEngines;
    private final Engines engines;
    private final Command command;

    public Config(Engines engines) {
        this.polyglotIsEnabled = true;
        this.contextEscapeIsEnabled = true;
        this.pollingIsEnabled = false;
        this.enabledEngines = new HashSet<>();
        this.engines = engines;
        this.command = new Command();
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
        this.enabledEngines.clear(); // Clear the set to disable all engines
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

    public void setPollingEnabled(boolean pollingIsEnabled) {
        this.pollingIsEnabled = pollingIsEnabled;
    }

    public boolean isPollingEnabled() {
        return this.pollingIsEnabled;
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

    /*
    public void enableLanguage(List<Engine> engines, Engine.Language language) {
        for (Engine engine : engines) {
            if (engine.getLanguage() == language) {
                enableEngine(engine.getName());
            }
        }
    }

    public void disableLanguage(List<Engine> engines, Engine.Language language) {
        for (Engine engine : engines) {
            if (engine.getLanguage() == language) {
                disableEngine(engine.getName());
            }
        }
    }
    */
}
