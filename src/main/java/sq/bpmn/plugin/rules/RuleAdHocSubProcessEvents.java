package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = RuleAdHocSubProcessEvents.RULE_KEY, name = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification", description = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification:\n" +
        "\n" +
        "Must not contain start or end events.\n" +
        "Every intermediate event must have an outgoing sequence flow.")
public class RuleAdHocSubProcessEvents implements BpmnRule{

    public static final String RULE_KEY = "ad-hoc-sub-process-start-event";

    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey,IssueMaker issueMaker)    {
        try{
            Elements elements = document.select(Helper.adHocSubProcess);
            for(Element element :elements){
            Elements startEvents = element.select(Helper.startEvent);
            for(Element startEvent :startEvents){
                issueMaker.newIssue(
                        startEvent,"A <Start Event> is not allowed in <Ad Hoc Sub Process>",file,sensorContext,ruleKey
                );
            }
                Elements endEvents = element.select(Helper.endEvent);
                for(Element endEvent :endEvents){
                    issueMaker.newIssue(
                            endEvent,"An <End Event> is not allowed in <Ad Hoc Sub Process>",file,sensorContext,ruleKey
                    );
                }

                Elements imEvents = element.select(Helper.intermediateCatchEvent+","+Helper.intermediateThrowEvent);
                for(Element imEvent :imEvents){
                    if(imEvent.select(Helper.outgoing).isEmpty()){
                        issueMaker.newIssue(
                                imEvent,"An intermediate event inside <Ad Hoc Sub Process> must have an outgoing sequence flow",file,sensorContext,ruleKey
                        );
                    }
                }
            }

        } catch (Exception ignored) {
            ;
        }


    }



}
