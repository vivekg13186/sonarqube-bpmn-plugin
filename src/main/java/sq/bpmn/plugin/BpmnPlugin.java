package sq.bpmn.plugin;

import org.sonar.api.Plugin;
import sq.bpmn.plugin.languages.BpmnLanguage;
import sq.bpmn.plugin.languages.BpmnQualityProfile;
import sq.bpmn.plugin.settings.BpmnLanguageProperties;

public class BpmnPlugin implements Plugin {
    @Override
    public void define(Context context) {

        context.addExtensions(BpmnLanguage.class,BpmnQualityProfile.class);

        context.addExtensions(BpmnLanguageProperties.getProperties());
        context.addExtensions(BpmnRulesDefinition.class,BpmnSensor.class);

    }
}