package sq.bpmn.plugin.rules;


import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.HashMap;

public class SubProcessBlankStartEventRule implements BpmnRule {
    public static final String RULE_KEY ="sub-process-blank-start-event";

    @Override
    public void check(BpmnModelInstance model, RuleReporter reporter) {
        model.getModelElementsByType(SubProcess.class).forEach(sp -> {
            sp.getChildElementsByType(StartEvent.class).forEach(se -> {
                if (se.getEventDefinitions().isEmpty()) {
                    reporter.report(se.getId(), "Embedded subprocess must not contain a blank start event");
                }
            });
        });
    }

    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {

    }
}