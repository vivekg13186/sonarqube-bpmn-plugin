package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = StartEventRequiredRule.RULE_KEY, name = "Start event required", description = " A rule that checks for the presence of a start event per scope.")
public class StartEventRequiredRule implements BpmnRule {
    public static final String RULE_KEY = "start-event-required";


    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey,IssueMaker issueMaker)    {


    }

    public static boolean validate(Document document){
        boolean isValid = true;

        Elements processes = document.select("bpmn|Process, bpmn|SubProcess");
        for (Element process : processes) {
            if (process.select("bpmn|StartEvent").isEmpty()) {


                isValid = false;
            }
        }

        return isValid;
    }
}


