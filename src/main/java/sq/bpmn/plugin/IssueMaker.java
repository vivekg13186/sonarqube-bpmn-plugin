package sq.bpmn.plugin;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;

public interface IssueMaker {

    void newIssue(int lno, String message, InputFile file, SensorContext context, RuleKey key);
}
