package sq.bpmn.plugin;

import org.sonar.api.server.rule.RulesDefinition;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static sq.bpmn.plugin.Constants.*;

public class BpmnRuleDefinition implements RulesDefinition {
    public static String readFile(String fileName) {
        InputStream inputStream = BpmnRuleDefinition.class.getClassLoader().getResourceAsStream("html/"+fileName+".html");
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
    @Override
    public void define(Context context) {
        NewRepository repo = context.createRepository(BpmnRepo, BpmnLanguage.KEY).setName("BPMN Rules");

        repo.createRule(ID_GlobalElement)
                .setName("global-elements")
                .setHtmlDescription("Global elements with missing name unused or not unique") ;
        repo.createRule(ID_AdHocSubProcess)
                .setName("ad-hoc-sub-process")
                .setHtmlDescription(readFile(ID_AdHocSubProcess)) ;

        repo.createRule(ID_ConditionalFlows)
                .setName("conditional-flows")
                .setHtmlDescription(readFile(ID_ConditionalFlows)) ;

        repo.createRule(ID_EndEventRequired)
                .setName("end-event-required")
                .setHtmlDescription(readFile(ID_EndEventRequired)) ;

        repo.createRule(ID_EventSubProcessTypedStartEvent)
                .setName("event-sub-process-typed-start-event")
                .setHtmlDescription(readFile(ID_EventSubProcessTypedStartEvent)) ;

        repo.createRule(ID_FakeJoin)
                .setName("fake-join")
                .setHtmlDescription(readFile(ID_FakeJoin)) ;


        repo.createRule(ID_LabelRequired)
                .setName("label-required")
                .setHtmlDescription(readFile(ID_LabelRequired)) ;

        repo.createRule(ID_LinkEvent)
                .setName("link-event")
                .setHtmlDescription(readFile(ID_LinkEvent)) ;

        repo.createRule(ID_NoBPMNDI)
                .setName("no-bpmndi")
                .setHtmlDescription(readFile(ID_NoBPMNDI)) ;

        repo.createRule(ID_NoComplexGateway)
                .setName("no-complex-gateway")
                .setHtmlDescription(readFile(ID_NoComplexGateway)) ;

        repo.createRule(ID_NoDisconnected)
                .setName("no-disconnected")
                .setHtmlDescription(readFile(ID_NoDisconnected)) ;

        repo.createRule(ID_NoDuplicateSequenceFlows)
                .setName("no-duplicate-sequence-flows")
                .setHtmlDescription(readFile(ID_NoDuplicateSequenceFlows)) ;

        repo.createRule(ID_NoGatewayJoinFork)
                .setName("no-gateway-join-fork")
                .setHtmlDescription(readFile(ID_NoGatewayJoinFork)) ;

        repo.createRule(ID_NoImplicitEnd )
                .setName("no-implicit-end")
                .setHtmlDescription(readFile(ID_NoImplicitEnd)) ;

        repo.createRule(ID_NoImplicitSplit)
                .setName("no-implicit-split")
                .setHtmlDescription(readFile(ID_NoImplicitSplit)) ;

        repo.createRule(ID_NoImplicitStart)
                .setName("no-implicit-start")
                .setHtmlDescription(readFile(ID_NoImplicitStart)) ;

        repo.createRule(ID_NoInclusiveGateway )
                .setName("no-inclusive-gateway")
                .setHtmlDescription(readFile(ID_NoInclusiveGateway)) ;

        repo.createRule(ID_NoOverlappingElements)
                .setName("no-overlapping-elements")
                .setHtmlDescription(readFile(ID_NoOverlappingElements)) ;

        repo.createRule(ID_SingleBlankStartEvent )

                .setName("single-blank-start-event")
                .setHtmlDescription(readFile(ID_SingleBlankStartEvent)) ;

        repo.createRule(ID_SingleEventDefinition )
                .setName("single-event-definition")
                .setHtmlDescription(readFile(ID_SingleEventDefinition)) ;

        repo.createRule(ID_StartEventRequired)
                .setName("start-event-required")
                .setHtmlDescription(readFile(ID_StartEventRequired)) ;

        repo.createRule(ID_SubProcessBlankStartEvent)
                .setName("sub-process-blank-start-event")
                .setHtmlDescription(readFile(ID_SubProcessBlankStartEvent)) ;

        repo.createRule(ID_SuperfluousGateway)
                .setName("superfluous-gateway")
                .setHtmlDescription(readFile(ID_SuperfluousGateway)) ;

        repo.createRule(ID_SuperfluousTermination)
                .setName("superfluous-termination")
                .setHtmlDescription(readFile(ID_SuperfluousTermination)) ;


        repo.done();
    }
}