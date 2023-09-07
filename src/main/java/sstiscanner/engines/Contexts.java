package sstiscanner.engines;

import java.util.ArrayList;
import java.util.List;

public class Contexts {

    static final List<String> VARIATIONS = List.of("", "'", "\"", ")", "')", "\")");

    // Python
    static final List<String> JINJA2_CONTEXT = List.of("}}*", "%}*");
    static final List<String> MAKO_CONTEXT = List.of("}*", "%>*<%#");
    static final List<String> TORNADO_CONTEXT = List.of("}}*", "%}*");

    // PHP
    static final List<String> SMARTY_CONTEXT = List.of("}*{");
    static final List<String> TWIG_CONTEXT = List.of("}}*{{1", "%}*");

    // Java
    static final List<String> FREEMARKER_CONTEXT = List.of("}*");
    static final List<String> VELOCITY_CONTEXT = List.of(")*");
    static final List<String> PEBBLE_CONTEXT = List.of("}}*", "%}*");
    static final List<String> THYMELEAF_CONTEXT = List.of("]]*", "}*");

    // JavaScript
    static final List<String> NUNJUCKS_CONTEXT = List.of("}}*{{1", "%}*");
    static final List<String> DOT_CONTEXT = List.of(";}}*{{1;");
    static final List<String> MARKO_CONTEXT = List.of("}*${\"1\"");
    static final List<String> EJS_CONTEXT = List.of("%>*<%#");
    static final List<String> HANDLEBARS_CONTEXT = List.of("}}*");
    static final List<String> PUG_CONTEXT = List.of(")*//");

    // Ruby
    static final List<String> SLIM_CONTEXT = List.of("}*");
    static final List<String> ERB_CONTEXT = List.of("%>*<%#");

    // ASP.NET
    static final List<String> RAZOR_CONTEXT = List.of(";}*@{1;");


    public static List<String> generateCodeEscapePayloads(String base, List<String> contexts) {
        List<String> escaped = new ArrayList<>();
        for (String context : contexts) {
            for (String variation : VARIATIONS) {
                escaped.add("1" + variation + context.replace("*", base));
            }
        }
        return escaped;
    }
}