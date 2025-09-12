package sq.bpmn.plugin.rules;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.HashMap;
import java.util.Objects;
@Rule(key =NoUnnamedStartEventRule.RULE_KEY, name = " ")
public class NoUnnamedStartEventRule implements BpmnRule {

    public static final String RULE_KEY ="no-unnamed-start-event";
    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String,Integer> lineMaps) {
        model.getModelElementsByType(StartEvent.class).forEach(start -> {
            if (start.getName() == null || start.getName().isBlank()) {
                int lno=Objects.requireNonNullElse(lineMaps.get(start.getId()),1);
                issueMaker.newIssue(lno,"Start event must have a name",file,sensorContext,ruleKey);
            }
        });
    }
}
