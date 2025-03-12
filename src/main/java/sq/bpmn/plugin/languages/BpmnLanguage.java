package sq.bpmn.plugin.languages;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;
import sq.bpmn.plugin.settings.BpmnLanguageProperties;


public class BpmnLanguage extends AbstractLanguage {
    public static final String KEY = "bpmn";
    public static final String NAME = "BPMN";

    private final Configuration config;

    public BpmnLanguage(Configuration config) {
        super(KEY, NAME);
        this.config = config;
    }

    @Override
    public String[] getFileSuffixes() {
        return config.getStringArray(BpmnLanguageProperties.FILE_SUFFIXES_KEY);
    }
}