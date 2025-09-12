package sq.bpmn.plugin.rules;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ManualTask;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.HashMap;
import java.util.Objects;

@Rule(key ="no-manual-task", name = " ")
public class NoManualTaskRule implements BpmnRule {
    public static final String RULE_KEY ="no-manual-task";
    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String,Integer> lineMaps) {
        model.getModelElementsByType(ManualTask.class).forEach(task -> {
            int lno= Objects.requireNonNullElse(lineMaps.get(task.getId()),1);
            issueMaker.newIssue(lno,"Manual tasks are not allowed",file,sensorContext,ruleKey);
        });
    }

}