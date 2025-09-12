package sq.bpmn.plugin.rules;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SubProcess;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@Rule(key = "end-event-required", name = "End Event Required", description = "Ensures that every process and sub-process has an end event. Explicitly modeling it improves the understandability of drawn process diagrams.")

public class EndEventRequiredRule implements BpmnRule {

    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String,Integer> lineMaps) {
        Collection<Process> processes= model.getModelElementsByType(Process.class);
        for(Process process : processes){
            if(process.getChildElementsByType(EndEvent.class).isEmpty()){
                int lno= Objects.requireNonNullElse(lineMaps.get(process.getId()),1);
                issueMaker.newIssue(lno,"Process is missing end event",file,sensorContext,ruleKey);
            }
        }
        Collection<SubProcess> subProcesses= model.getModelElementsByType(SubProcess.class);
        for(SubProcess process : subProcesses){
            if(process.getChildElementsByType(EndEvent.class).isEmpty()){
                int lno= Objects.requireNonNullElse(lineMaps.get(process.getId()),1);
                issueMaker.newIssue(lno,"Sub Process is missing end event",file,sensorContext,ruleKey);
            }
        }


    }
}
