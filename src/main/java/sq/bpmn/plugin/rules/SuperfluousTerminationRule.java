package sq.bpmn.plugin.rules;



import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.TerminateEventDefinition;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.HashMap;

public class SuperfluousTerminationRule implements BpmnRule {
    public static final String RULE_KEY ="superfluous-termination";

    @Override
    public void check(BpmnModelInstance model, RuleReporter reporter) {
        boolean hasNormalEnd = model.getModelElementsByType(EndEvent.class).stream()
                .anyMatch(e -> e.getEventDefinitions().stream().noneMatch(TerminateEventDefinition.class::isInstance));

        if (hasNormalEnd) {
            model.getModelElementsByType(EndEvent.class).forEach(e -> {
                if (e.getEventDefinitions().stream().anyMatch(TerminateEventDefinition.class::isInstance)) {
                    reporter.report(e.getId(), "Terminate end event is superfluous since a normal end exists");
                }
            });
        }
    }

    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {

    }
}