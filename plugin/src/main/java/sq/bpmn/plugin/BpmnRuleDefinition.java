package sq.bpmn.plugin;

import org.sonar.api.server.rule.RulesDefinition;

import static sq.bpmn.plugin.Constants.*;

public class BpmnRuleDefinition implements RulesDefinition {
    @Override
    public void define(Context context) {
        NewRepository repo = context.createRepository(BpmnRepo, BpmnLanguage.KEY).setName("BPMN Rules");


        repo.createRule(ID_AdHocSubProcess)
                .setName("Start event is missing1")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_ConditionalFlows)
                .setName("Start event is missing2")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_EndEventRequired)
                .setName("Start event is missing3")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_EventSubProcessTypedStartEvent)
                .setName("Start event is missing4")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_FakeJoin)
                .setName("Start event is missing5")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;


        repo.createRule(ID_LabelRequired)
                .setName("Start event is missin6g")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_LinkEvent)
                .setName("Start event is missing7")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoBPMNDI)
                .setName("Start event is missing8")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoComplexGateway)
                .setName("Start event is missing9")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoDisconnected)
                .setName("Start event is missing11")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoDuplicateSequenceFlows)
                .setName("Start event is missing12")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoGatewayJoinFork)
                .setName("Start event is missing13")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoImplicitEnd )
                .setName("Start event is missing")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoImplicitSplit)
                .setName("Start event is missing14")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoImplicitStart)
                .setName("Start event is missing15")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoInclusiveGateway )
                .setName("Start event is missing16")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_NoOverlappingElements)
                .setName("Start event is missing17")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SingleBlankStartEvent )
                .setName("Start event is missing18")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SingleEventDefinition )
                .setName("Start event is missing19")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_StartEventRequired)
                .setName("Start event is missing20")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SubProcessBlankStartEvent)
                .setName("Start event is missing21")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SuperfluousGateway)
                .setName("Start event is missing22")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;

        repo.createRule(ID_SuperfluousTermination)
                .setName("Start event is missing23")
                .setHtmlDescription("Each BPMN process should start with a start event.") ;


        repo.done();
    }
}