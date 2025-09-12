package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

@Rule(key = FakeJoinsRule.RULE_KEY, name = "Fake Join", description = "Checks that no fake join is modeled by attempting to give a task or event join semantics. Users should model a parallel joining gateway to achieve the desired behavior.")

public class FakeJoinsRule implements BpmnRule {
    public static final String RULE_KEY = "fake-joins";


    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker)    {
        try{
            String activitySelector = "*|adhocSubProcess," +
                    "*|businessRuleTask," +
                    "*|callActivity," +
                    "*|manualTask," +
                    "*|receiveTask," +
                    "*|scriptTask," +
                    "*|sendTask," +
                    "*|serviceTask," +
                    "*|subProcess," +
                    "*|task," +
                    "*|transaction," +
                    "*|userTask";
            Elements activities = document.select(activitySelector);

            for (Element activity : activities) {
                if(activity.select("*|incoming").size()>1){
                    issueMaker.newIssue(activity,"Incoming flows do not join",file,sensorContext,ruleKey);
                }
            }
            String eventSelector = "*|boundaryEvent," +
                    "*|catchEvent," +
                    "*|endEvent," +
                    "*|implicitThrowEvent," +
                    "*|intermediateCatchEvent," +
                    "*|intermediateThrowEvent," +
                    "*|startEvent," +
                    "*|throwEvent" ;
            Elements events = document.select(eventSelector);

            for (Element event : events) {
                if(event.select("*|incoming").size()>1){
                    issueMaker.newIssue(event,"Incoming flows do not join",file,sensorContext,ruleKey);
                }
            }
        } catch (Exception ignored) {
            ;
        }


    }

}
