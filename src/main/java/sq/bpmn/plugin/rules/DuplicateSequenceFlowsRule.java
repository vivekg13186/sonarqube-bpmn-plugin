package sq.bpmn.plugin.rules;


import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.*;
@Rule(key = DuplicateSequenceFlowsRule.RULE_KEY, name = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification", description = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification:\n" +
        "\n" +
        "Must not contain start or end events.\n" +
        "Every intermediate event must have an outgoing sequence flow.")
public class DuplicateSequenceFlowsRule implements BpmnRule {
    public static final String RULE_KEY =  "duplicate-sequence-flows";



    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {
        Map<String, List<SequenceFlow>> seen = new HashMap<>();
        model.getModelElementsByType(SequenceFlow.class).forEach(flow -> {
            String key = flow.getSource().getId() + "->" + flow.getTarget().getId();
            seen.computeIfAbsent(key, k -> new ArrayList<>()).add(flow);
        });
        seen.forEach((k, flows) -> {
            if (flows.size() > 1) {
                flows.forEach(f -> reporter.report(f.getId(),
                        "Duplicate sequence flow between " + f.getSource().getName() + " and " + f.getTarget().getName()));
            }
        });
    }
}
