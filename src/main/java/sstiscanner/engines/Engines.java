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
        addEngine("Jinja2", Payloads.JINJA2_PAYLOAD, Engine.Language.PYTHON, Contexts.JINJA2_CONTEXT);
        addEngine("Mako", Payloads.MAKO_PAYLOAD, Engine.Language.PYTHON, Contexts.JINJA2_CONTEXT);
        addEngine("Tornado", Payloads.TORNADO_PAYLOAD, Engine.Language.PYTHON, Contexts.JINJA2_CONTEXT);

        // PHP
        addEngine("Smarty", Payloads.SMARTY_PAYLOAD, Engine.Language.PHP, Contexts.JINJA2_CONTEXT);
        addEngine("Twig", Payloads.TWIG_PAYLOAD, Engine.Language.PHP, Contexts.JINJA2_CONTEXT);

        // Java
        addEngine("FreeMarker", Payloads.FREEMARKER_PAYLOAD, Engine.Language.JAVA, Contexts.JINJA2_CONTEXT);
        addEngine("Velocity", Payloads.VELOCITY_PAYLOAD, Engine.Language.JAVA, Contexts.JINJA2_CONTEXT);
        addEngine("Thymeleaf", Payloads.THYMELEAF_PAYLOAD, Engine.Language.JAVA, Contexts.JINJA2_CONTEXT);

        // JavaScript
        addEngine("Nunjucks", Payloads.NUNJUCKS_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        addEngine("doT", Payloads.DOT_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        addEngine("EJS", Payloads.EJS_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        addEngine("Vuejs", Payloads.VUEJS_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);
        addEngine("Jade", Payloads.JADE_PAYLOAD, Engine.Language.JAVASCRIPT, Contexts.JINJA2_CONTEXT);

        // Ruby
        addEngine("Slim", Payloads.SLIM_PAYLOAD, Engine.Language.RUBY, Contexts.JINJA2_CONTEXT);
        addEngine("ERB", Payloads.ERB_PAYLOAD, Engine.Language.RUBY, Contexts.JINJA2_CONTEXT);

        // ASP.NET
        addEngine("Razor", Payloads.RAZOR_PAYLOAD, Engine.Language.ASPNET, Contexts.JINJA2_CONTEXT);
    }

    private void addEngine(String name, String payload, Engine.Language language, List<String> contexts) {
        engines.add(new Engine(name, payload, language, contexts));
    }

    public List<Engine> getEngines() {
        return Collections.unmodifiableList(engines);
    }
}