package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = RuleEventSubProcessTypedStartEvent.RULE_KEY, name = "Event Sub Process Typed Start Event", description = "Ensures that start events inside event sub-processes are typed (have an event definition). This is required by the BPMN 2.0 standard.")

public class RuleEventSubProcessTypedStartEvent {

    public static final String RULE_KEY ="event-sub-process-typed-start-event";

    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker) {
        Elements processes =   document.select("*|subProcess");
        for (Element process : processes) {

            if (process.select("*|cancelEventDefinition, *|compensateEventDefinition,*|conditionalEventDefinition,*|errorEventDefinition,*|escalationEventDefinition,*|linkEventDefinition,*|messageEventDefinition,*|signalEventDefinition,*|terminateEventDefinition,*|timerEventDefinition").isEmpty()) {
                issueMaker.newIssue(process,"Start event is missing event definition",file,sensorContext,ruleKey);
            }
        }

    }
}
