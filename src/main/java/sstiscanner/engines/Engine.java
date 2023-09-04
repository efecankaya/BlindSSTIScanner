package sstiscanner.engines;

import java.util.List;

public class Engine {
    private final String name;
    private final String payload;
    private final Language language;
    private final List<String> contexts;

    public enum Language {
        PYTHON,
        PHP,
        JAVA,
        JAVASCRIPT,
        RUBY,
        ASPNET
    }

    public Engine(String name, String payload, Language language, List<String> contexts) {
        this.name = name;
        this.payload = payload;
        this.language = language;
        this.contexts = contexts;
    }

    public String getName() {
        return this.name;
    }

    public String getPayload() {
        return this.payload;
    }

    public Language getLanguage() {
        return this.language;
    }

}
