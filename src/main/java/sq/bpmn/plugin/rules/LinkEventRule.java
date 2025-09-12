package sq.bpmn.plugin.rules;




import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.*;
import java.util.stream.Collectors;
@Rule(key = AdHocSubProcessEventsRule.RULE_KEY, name = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification", description = "Ensure that an Ad-Hoc Sub-Process is valid according to the BPMN specification:\n" +
        "\n" +
        "Must not contain start or end events.\n" +
        "Every intermediate event must have an outgoing sequence flow.")
public class LinkEventRule implements BpmnRule {
    public static final String RULE_KEY = "link-event";

    @Override
    public void check(BpmnModelInstance model, RuleReporter reporter) {
        List<IntermediateCatchEvent> catchEvents = model.getModelElementsByType(IntermediateCatchEvent.class)
                .stream().filter(e -> e.getEventDefinitions().stream().anyMatch(LinkEventDefinition.class::isInstance))
                .collect(Collectors.toList());

        List<IntermediateThrowEvent> throwEvents = model.getModelElementsByType(IntermediateThrowEvent.class)
                .stream().filter(e -> e.getEventDefinitions().stream().anyMatch(LinkEventDefinition.class::isInstance))
                .collect(Collectors.toList());

        Set<String> catchNames = catchEvents.stream()
                .flatMap(e -> e.getEventDefinitions().stream())
                .filter(LinkEventDefinition.class::isInstance)
                .map(d -> ((LinkEventDefinition) d).getName())
                .collect(Collectors.toSet());

        Set<String> throwNames = throwEvents.stream()
                .flatMap(e -> e.getEventDefinitions().stream())
                .filter(LinkEventDefinition.class::isInstance)
                .map(d -> ((LinkEventDefinition) d).getName())
                .collect(Collectors.toSet());

        for (String name : throwNames) {
            if (!catchNames.contains(name)) {
                reporter.report("LINK_THROW", "Link throw event '" + name + "' has no matching catch");
            }
        }
        for (String name : catchNames) {
            if (!throwNames.contains(name)) {
                reporter.report("LINK_CATCH", "Link catch event '" + name + "' has no matching throw");
            }
        }
    }

    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {

    }
}
