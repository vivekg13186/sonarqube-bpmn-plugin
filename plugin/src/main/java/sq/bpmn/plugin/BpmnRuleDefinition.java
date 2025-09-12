package sq.bpmn.plugin;

import org.sonar.api.batch.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

import static sq.bpmn.plugin.Constants.*;

public class BpmnRuleDefinition implements RulesDefinition {
    @Override
    public void define(Context context) {
        NewRepository repo = context.createRepository("bpmn-repo", BpmnLanguage.KEY).setName("BPMN Rules");


        repo.createRule(ID_AdHocSubProcess)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_ConditionalFlows)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_EndEventRequired)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_EventSubProcessTypedStartEvent)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_FakeJoin)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_GlobalElement)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_LabelRequired)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_LinkEvent)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoBPMNDI)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoComplexGateway)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoDisconnected)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoDuplicateSequenceFlows)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoGatewayJoinFork)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoImplicitEnd )
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoImplicitSplit)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoImplicitStart)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoInclusiveGateway )
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoOverlappingElements)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SingleBlankStartEvent )
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SingleEventDefinition )
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_StartEventRequired)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SubProcessBlankStartEvent)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SuperfluousGateway)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SuperfluousTermination)
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;


        repo.done();
    }
}