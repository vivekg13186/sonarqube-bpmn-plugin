package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = SingleBlankStartEventRule.RULE_KEY, name = "Multiple start events", description = "A rule that checks whether not more than one blank start event exists per scope.")
public class SingleBlankStartEventRule implements BpmnRule {
    public static final String RULE_KEY = "single-blank-start-event";


    @Override
    public void execute(SensorContext sensorContext, Document  document,InputFile file, RuleKey ruleKey)    {
        try{
            if(!validate(document)){
                NewIssue newIssue = sensorContext.newIssue();
                newIssue
                        .forRule(ruleKey)
                        .at(newIssue.newLocation()
                                .on(file)
                                .at(file.selectLine(1)))
                        .save();
            }
        } catch (Exception ignored) {
             ;
        }


    }

    public static boolean validate(Document document){
        Elements elements = document.select("bpmn|startEvent");
        int blankStarts=0;
        for (Element element : elements) {
            if(element.select("bpmn|eventDefinitions").isEmpty()){
                blankStarts++;
            }
        }
        return blankStarts <= 1;
    }
}
