package sq.bpmn.plugin.rules;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;


import java.util.HashMap;
import java.util.Objects;

@Rule(key = AdHocSubProcessEventsRule.RULE_KEY, name = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification", description = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification:\n" +
        "\n" +
        "Must not contain start or end events.\n" +
        "Every intermediate event must have an outgoing sequence flow.")
public class AdHocSubProcessEventsRule implements BpmnRule {

    public static final String RULE_KEY = "ad-hoc-sub-process-start-event";



    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {
        for (ModelElementInstance el : model.getModelElementsByType(ModelElementInstance.class)) {

            if ("adHocSubProcess".equals(el.getElementType().getTypeName())) {

                for (StartEvent se : el.getChildElementsByType(StartEvent.class)) {
                    int lno= Objects.requireNonNullElse(lineMap.get(se.getId()),1);
                    issueMaker.newIssue(
                            lno,"A <Start Event> is not allowed in <Ad Hoc Sub Process>",file,sensorContext,ruleKey
                    );
                }
                for (EndEvent se : el.getChildElementsByType(EndEvent.class)) {
                    int lno= Objects.requireNonNullElse(lineMap.get(se.getId()),1);
                    issueMaker.newIssue(
                            lno,"An <End Event> is not allowed in <Ad Hoc Sub Process>",file,sensorContext,ruleKey
                    );
                }
                for (IntermediateCatchEvent imEvent : el.getChildElementsByType(IntermediateCatchEvent.class)) {
                        if(imEvent.getOutgoing().isEmpty()){
                            int lno= Objects.requireNonNullElse(lineMap.get(imEvent.getId()),1);
                            issueMaker.newIssue(
                                    lno,"An intermediate event inside <Ad Hoc Sub Process> must have an outgoing sequence flow",file,sensorContext,ruleKey
                            );
                        }
                }
            }
        }
    }
}
