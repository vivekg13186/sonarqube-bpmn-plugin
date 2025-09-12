package sq.bpmn.plugin;

import org.sonar.api.resources.Language;

public class BpmnLanguage implements Language {
    public static final String KEY = "bpmn";
    public static final String NAME = "BPMN";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getFileSuffixes() {
        return new String[] {".bpmn", ".bpmn20.xml"};
    }
}
