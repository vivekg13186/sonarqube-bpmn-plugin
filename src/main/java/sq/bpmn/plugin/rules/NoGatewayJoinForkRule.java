package sq.bpmn.plugin.rules;


import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.HashMap;
@Rule(key = AdHocSubProcessEventsRule.RULE_KEY, name = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification", description = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification:\n" +
        "\n" +
        "Must not contain start or end events.\n" +
        "Every intermediate event must have an outgoing sequence flow.")
public class NoGatewayJoinForkRule implements BpmnRule {
    public static final String RULE_KEY ="no-gateway-join-fork";

    @Override
    public void check(BpmnModelInstance model, RuleReporter reporter) {
        model.getModelElementsByType(Gateway.class).forEach(gw -> {
            if (!gw.getIncoming().isEmpty() && gw.getIncoming().size() > 1 &&
                    !gw.getOutgoing().isEmpty() && gw.getOutgoing().size() > 1) {
                reporter.report(gw.getId(), "Gateway acts as both join and fork");
            }
        });
    }

    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {

    }
}
