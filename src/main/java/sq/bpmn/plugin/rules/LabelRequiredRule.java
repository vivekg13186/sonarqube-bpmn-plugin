package sq.bpmn.plugin.rules;


import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.HashMap;
@Rule(key = LabelRequiredRule.RULE_KEY, name = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification", description = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification:\n" +
        "\n" +
        "Must not contain start or end events.\n" +
        "Every intermediate event must have an outgoing sequence flow.")
public class LabelRequiredRule implements BpmnRule {
    public static final String RULE_KEY ="label-required";

    @Override
    public void check(BpmnModelInstance model, RuleReporter reporter) {
        model.getModelElementsByType(BaseElement.class).forEach(el -> {
            if (el instanceof org.camunda.bpm.model.bpmn.instance.FlowNode
                    || el instanceof org.camunda.bpm.model.bpmn.instance.Gateway) {
                String name = ((org.camunda.bpm.model.xml.instance.ModelElementInstance) el).getAttributeValue("name");
                if (name == null || name.isBlank()) {
                    reporter.report(el.getId(), "Element must have a label");
                }
            }
        });
    }

    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {

    }
}
