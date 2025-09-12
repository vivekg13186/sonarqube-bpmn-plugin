package sq.bpmn.plugin;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;

import java.util.HashMap;

public interface BpmnRule {

    void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String,Integer> lineMap);
}
