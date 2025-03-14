package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;

public interface BpmnRule {
    void execute(SensorContext sensorContext,  Document document,InputFile file, RuleKey ruleKey,IssueMaker issueMaker);
}
