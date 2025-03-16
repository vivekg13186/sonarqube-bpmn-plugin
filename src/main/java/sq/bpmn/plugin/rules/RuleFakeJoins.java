package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = RuleFakeJoins.RULE_KEY, name = "Fake Join", description = "Checks that no fake join is modeled by attempting to give a task or event join semantics. Users should model a parallel joining gateway to achieve the desired behavior.")

public class RuleFakeJoins  implements BpmnRule{
    public static final String RULE_KEY = "fake-joins";


    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker)    {
        try{
            Elements elements = document.select(Helper.startEvent);
            int blankStarts=0;
            for (Element element : elements) {
                if(element.select(Helper.eventDefinitions).isEmpty()){
                    blankStarts++;
                }
            }
            if(blankStarts > 1){
                issueMaker.newIssue(elements.last(),"sada",file,sensorContext,ruleKey);
            }
        } catch (Exception ignored) {
            ;
        }


    }

}
