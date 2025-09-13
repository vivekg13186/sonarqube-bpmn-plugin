package sq.bpmn.plugin;

import org.sonar.api.Plugin;

public class BpmnPlugin implements Plugin {
    @Override
    public void define(Context context) {
        context.addExtensions(
                BpmnLanguage.class,
                BpmnSensor.class,
                BpmnRuleDefinition.class,BpmnRecommendedProfile.class // Your rule definitions
        );
    }
}