package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Element;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;

public class PluginIssueMaker implements IssueMaker{
    @Override
    public void newIssue(Element element, String message, InputFile file, SensorContext sensorContext, RuleKey ruleKey) {
        NewIssue newIssue = sensorContext.newIssue();
        newIssue.forRule(ruleKey);
        Helper.setLocation(file,element, newIssue.newLocation(),message);
    }
}
