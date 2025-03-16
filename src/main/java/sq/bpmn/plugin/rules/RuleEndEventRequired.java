package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = RuleEndEventRequired.RULE_KEY, name = "End Event Required", description = "Ensures that every process and sub-process has an end event. Explicitly modeling it improves the understandability of drawn process diagrams.")

public class RuleEndEventRequired implements BpmnRule{
    public static final String RULE_KEY = "end-event-required";
    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker) {
        Elements processes = document.select("*|process");
        for (Element process : processes) {
            if (process.select("*|endEvent").isEmpty()) {
                issueMaker.newIssue(process,"Process is missing end event",file,sensorContext,ruleKey);
            }
        }
          processes = document.select("*|subProcess");
        for (Element process : processes) {
            if (process.select("*|endEvent").isEmpty()) {
                issueMaker.newIssue(process,"Sub Process is missing end event",file,sensorContext,ruleKey);
            }
        }

    }
}
