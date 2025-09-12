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

@Rule(key = SingleEventDefinitionRule.RULE_KEY, name = "Single Event Definition", description = "Checks that an event contains no more than one event definition.")

public class SingleEventDefinitionRule implements BpmnRule {
    public static final String RULE_KEY = "single-event-definition";
    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker) {
        String eventSelector = "*|boundaryEvent," +
                "*|catchEvent," +
                "*|endEvent," +
                "*|implicitThrowEvent," +
                "*|intermediateCatchEvent," +
                "*|intermediateThrowEvent," +
                "*|startEvent," +
                "*|throwEvent" ;
        Elements events = document.select(eventSelector);
        String eventDefinitionSelector="*|cancelEventDefinition," +
                "*|errorEventDefinition," +
                "*|terminateEventDefinition," +
                "*|escalationEventDefinition," +
                "*|compensateEventDefinition," +
                "*|timerEventDefinition," +
                "*|linkEventDefinition," +
                "*|messageEventDefinition," +
                "*|conditionalEventDefinition," +
                "*|signalEventDefinition";
        for(Element event : events){
            if(event.select(eventDefinitionSelector).size()>1){
                issueMaker.newIssue(event,"Event has multiple event definitions",file,sensorContext,ruleKey);
            }

        }

    }
}
