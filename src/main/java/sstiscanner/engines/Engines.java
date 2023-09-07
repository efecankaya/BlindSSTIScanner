package sstiscanner.engines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Engines {

    private final List<Engine> engines = new ArrayList<>();
    private final List<String> polyglots = List.of("${{<%[%'\"}}%\\", "<th:t=\\\"${xu}#foreach", "<%={{={@{#{${xu}}%>");

    public Engines() {
        initializeEngines();
    }

    private void initializeEngines() {
        // Python
        this.addEngine("Jinja2", Payloads.JINJA2_PAYLOAD, Engine.Language.PYTHON, Contexts.JINJA2_CONTEXT);
        this.addEngine("Mako", Payloads.MAKO_PAYLOAD, Engine.Language.PYTHON, Contexts.JINJA2_CONTEXT);
        this.addEngine("Tornado", Payloads.TORNADO_PAYLOAD, Engine.Language.PYTHON, Contexts.JINJA2_CONTEXT);

        // PHP
        this.addEngine("Smarty", Payloads.SMARTY_PAYLOAD, Engine.Language.PHP, Contexts.JINJA2_CONTEXT);
        this.addEngine("Twig", Payloads.TWIG_PAYLOAD, Engine.Language.PHP, Contexts.JINJA2_CONTEXT);

        // Java
        this.addEngine("FreeMarker", Payloads.FREEMARKER_PAYLOAD, Engine.Language.JAVA, Contexts.JINJA2_CONTEXT);
        this.addEngine("Velocity", Payloads.VELOCITY_PAYLOAD, Engine.Language.JAVA, Contexts.JINJA2_CONTEXT);
        this.addEngine("Thymeleaf", Payloads.THYMELEAF_PAYLOAD, Engine.Language.JAVA, Contexts.JINJA2_CONTEXT);
        this.addEngine("Pebble", Payloads.PEBBLE_PAYLOAD, Engine.Language.JAVA, Contexts.JINJA2_CONTEXT);

        // JavaScript
        this.addEngine("Nunjucks", Payloads.NUNJUCKS_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        this.addEngine("doT", Payloads.DOT_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        this.addEngine("EJS", Payloads.EJS_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        this.addEngine("Marko", Payloads.MARKO_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        this.addEngine("Pug", Payloads.PUG_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        this.addEngine("Handlebars", Payloads.HANDLEBARS_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);

        // Ruby
        this.addEngine("Slim", Payloads.SLIM_PAYLOAD, Engine.Language.RUBY, Contexts.JINJA2_CONTEXT);
        this.addEngine("ERB", Payloads.ERB_PAYLOAD, Engine.Language.RUBY, Contexts.JINJA2_CONTEXT);

        // ASP.NET
        this.addEngine("Razor", Payloads.RAZOR_PAYLOAD, Engine.Language.ASPNET, Contexts.JINJA2_CONTEXT);
    }

    private void addEngine(String name, String payload, Engine.Language language, List<String> contexts) {
        this.engines.add(new Engine(name, payload, language, contexts));
    }

    public List<Engine> getEngines() {
        return Collections.unmodifiableList(this.engines);
    }

    public List<String> getPolyglots() {
        return this.polyglots;
    }
}