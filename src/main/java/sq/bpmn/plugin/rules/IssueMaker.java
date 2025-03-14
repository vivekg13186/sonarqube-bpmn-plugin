package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Element;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;

public interface IssueMaker {

    void newIssue(Element element, String message, InputFile file, SensorContext context, RuleKey key);
}
