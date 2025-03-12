package sq.bpmn.plugin;

import org.sonar.api.Plugin;
import sq.bpmn.plugin.languages.BpmnLanguage;
import sq.bpmn.plugin.languages.BpmnQualityProfile;
import sq.bpmn.plugin.settings.BpmnLanguageProperties;

public class BpmnPlugin implements Plugin {
    @Override
    public void define(Context context) {

        context.addExtension(BpmnLanguage.class);
        context.addExtension(BpmnQualityProfile.class);
        context.addExtensions(BpmnLanguageProperties.getProperties());
        context.addExtension(BpmnSensor.class);
        context.addExtension(BpmnRulesDefinition.class);
    }
}