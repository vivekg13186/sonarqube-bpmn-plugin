package com.bpmnlint;

import com.bpmnlint.validator.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import static org.junit.Assert.assertTrue;

public class RuleTest1 {

    public static String readFile(String fileName) {
        InputStream inputStream = RuleTest1.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
    public Document loadDoc(String path){
       return Jsoup.parse(readFile(path), Parser.xmlParser().setTrackPosition(true));
    }


    @Test
    public void adHocSubProcessValidator(){
        assert AdHocSubProcessValidator.validate(loadDoc("test/ad-hoc-sub-process-correct.bpmn")).isEmpty();
        List<Issue> issueList = AdHocSubProcessValidator.validate(loadDoc("test/ad-hoc-sub-process-incorrect.bpmn"));

        assertThat(issueList).extracting("id", "line", "message")
                .contains(
                        tuple("Event_0q40via", 5, "An intermediate catch event inside <Ad Hoc Sub Process> must have an outgoing sequence flow"),
                        tuple("Event_03k6pnd", 10, "A <Start Event> is not allowed in <Ad Hoc Sub Process>"),
                        tuple("Event_03gcp25", 13, "An <End Event> is not allowed in <Ad Hoc Sub Process>"));

    }
    @Test
    public void conditionalFlowsValidator(){
        assert ConditionalFlowsValidator.validate(loadDoc("test/conditional-flows-correct.bpmn")).isEmpty();
        List<Issue> issueList =ConditionalFlowsValidator.validate(loadDoc("test/conditional-flows-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(
                        tuple("SequenceFlow_0q9ussl", 15, "Sequence flow is missing condition") );

    }
    @Test
    public void endEventRequiredValidator(){
        assert EndEventRequiredValidator.validate(loadDoc("test/end-event-required-correct.bpmn")).isEmpty();
        List<Issue> issueList =EndEventRequiredValidator.validate(loadDoc("test/end-event-required-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(
                        tuple("Process_1qgckdh", 3, "Process is missing end event") );

    }

    @Test
    public void eventSubProcessTypedStartEventValidator(){
        assert EventSubProcessTypedStartEventValidator.validate(loadDoc("test/event-sub-process-typed-start-event-correct.bpmn")).isEmpty();
        List<Issue> issueList =EventSubProcessTypedStartEventValidator.validate(loadDoc("test/event-sub-process-typed-start-event-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(
                        tuple("StartEvent_0ydcpql", 5, "Start event is missing event definition") );

    }
    @Test
    public void fakeJoinValidator(){
         assert FakeJoinValidator.validate(loadDoc("test/fake-join-correct.bpmn")).isEmpty();
        List<Issue> issueList =FakeJoinValidator.validate(loadDoc("test/fake-join-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(
                        tuple("Task_2", 10, "Incoming flows do not join") );


    }

    @Test
    public void labelRequiredValidator(){
        assert LabelRequiredValidator.validate(loadDoc("test/label-required-correct.bpmn")).isEmpty();
        List<Issue> issueList =LabelRequiredValidator.validate(loadDoc("test/label-required-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(
                        tuple("StartEvent_1", 4, "Element is missing label/name") ,
                        tuple("Task_1", 7, "Element is missing label/name") ,
                        tuple("EndEvent_1", 12, "Element is missing label/name") );


    }

    @Test
    public void linkEventValidator(){
        List<Issue> issueList = LinkEventValidator.validate(loadDoc("test/link-event-correct.bpmn")) ;

        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Event_0fvzchs", 4, "Throw link event is missing a name"),
                        tuple("Event_0hnrksg", 15, "Catch link event is missing a name") );


        issueList =LinkEventValidator.validate(loadDoc("test/link-event-incorrect.bpmn"));
         assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Event_0fvzchs", 4, "Throw link event is missing a name"),
                        tuple("Event_0rw1f16", 23, "Catch link event is missing a name"),
                        tuple("Event_022ctsn", 13, "Catch link event is missing a name"),
                        tuple("Event_0q5xos0", 17, "Throw link event is missing a name"));


    }
    //TODO
    @Test
    public void noBPMNDIValidator(){
        List<Issue> issueList = NoBPMNDIValidator.validate(loadDoc("test/no-bpmndi-correct.bpmn"));
        assertTrue(issueList.isEmpty());
        issueList =NoBPMNDIValidator.validate(loadDoc("test/no-bpmndi-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("SequenceFlow_1", 7, "Element <bpmn:sequenceFlow> with id \"SequenceFlow_1\" is missing BPMNDI visual representation") );


    }
    @Test
    public void noComplexGatewayValidator (){
        assert NoComplexGatewayValidator .validate(loadDoc("test/no-complex-gateway-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoComplexGatewayValidator .validate(loadDoc("test/no-complex-gateway-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("ExclusiveGateway_2",29, "Use of <Complex Gateway> is discouraged") );
    }
    @Test
    public void noDisconnectedValidator (){
        assert NoDisconnectedValidator .validate(loadDoc("test/no-disconnected-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoDisconnectedValidator .validate(loadDoc("test/no-disconnected-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_2",16, "Element <bpmn:task> is not connected to any sequence flow") );

    }

    @Test
    public void noDuplicateSequenceFlowsValidator  (){
        assert NoDuplicateSequenceFlowsValidator  .validate(loadDoc("test/no-duplicate-sequence-flows-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoDuplicateSequenceFlowsValidator  .validate(loadDoc("test/no-duplicate-sequence-flows-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("SequenceFlow_4",23, "SequenceFlow is a duplicate") ,
                        tuple("Task_1",7, "Duplicate outgoing sequence flows") ,
                        tuple("Task_2",13, "Duplicate incoming sequence flows") );


    }

    @Test
    public void noGatewayJoinForkValidator   (){
        assert NoGatewayJoinForkValidator   .validate(loadDoc("test/no-gateway-join-fork-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoGatewayJoinForkValidator   .validate(loadDoc("test/no-gateway-join-fork-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("ExclusiveGateway_1",10, "Gateway forks and joins simultaneously, which may lead to ambiguous logic")  );

    }

    @Test
    public void noImplicitEndValidator  (){
        assert NoImplicitEndValidator    .validate(loadDoc("test/no-implicit-end-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoImplicitEndValidator    .validate(loadDoc("test/no-implicit-end-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_1",7, "Element is an implicit end (no outgoing sequence flow)")  );

    }

    @Test
    public void noImplicitSplitValidator     (){
        assert NoImplicitSplitValidator     .validate(loadDoc("test/no-implicit-split-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoImplicitSplitValidator     .validate(loadDoc("test/no-implicit-split-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_1",7, "Element has multiple outgoing flows but is not a gateway—implicit split detected")  );

    }

    @Test
    public void noImplicitStartValidator    (){
        assert NoImplicitStartValidator    .validate(loadDoc("test/no-implicit-start-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoImplicitStartValidator    .validate(loadDoc("test/no-implicit-start-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_1",4, "Element is an implicit start")  );

    }

    @Test
    public void noInclusiveGatewayValidator    (){
        assert NoInclusiveGatewayValidator    .validate(loadDoc("test/no-inclusive-gateway-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoInclusiveGatewayValidator    .validate(loadDoc("test/no-inclusive-gateway-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("InclusiveGateway_1",24, "Inclusive gateways are discouraged")  ,
                        tuple("InclusiveGateway_2",29, "Inclusive gateways are discouraged")  );


    }

    @Test
    public void noOverlappingElementsValidator    (){
        assert NoOverlappingElementsValidator    .validate(loadDoc("test/no-overlapping-elements-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoOverlappingElementsValidator    .validate(loadDoc("test/no-overlapping-elements-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Activity_1v5k805",7, "Overlaps with element 'Activity_0g4fgz0'")  ,
                        tuple("Activity_0g4fgz0",4, "Overlaps with element 'Activity_1v5k805'")  );


    }

    @Test
    public void singleBlankStartEventValidator    (){
        assert SingleBlankStartEventValidator    .validate(loadDoc("test/single-blank-start-event-correct.bpmn")).isEmpty();
        List<Issue> issueList =SingleBlankStartEventValidator    .validate(loadDoc("test/single-blank-start-event-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Process_1",3, "Process has multiple blank start events")   );

    }



    @Test
    public void singleEventDefinitionValidator(){
        assert SingleEventDefinitionValidator    .validate(loadDoc("test/single-event-definition-correct.bpmn")).isEmpty();
        List<Issue> issueList =SingleEventDefinitionValidator    .validate(loadDoc("test/single-event-definition-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("IntermediateEvent_1",4, "Event has multiple event definitions")   );


    }

    @Test
    public void startEventRequiredValidator(){
        assert StartEventRequiredValidator     .validate(loadDoc("test/start-event-required-correct.bpmn")).isEmpty();
        List<Issue> issueList =StartEventRequiredValidator     .validate(loadDoc("test/start-event-required-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Process_1",3, "Process is missing start event")   );

    }

    @Test
    public void subProcessBlankStartEventValidator    (){
        assert SubProcessBlankStartEventValidator    .validate(loadDoc("test/sub-process-blank-start-event-correct.bpmn")).isEmpty();
        List<Issue> issueList =SubProcessBlankStartEventValidator    .validate(loadDoc("test/sub-process-blank-start-event-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("StartEvent_1",5, "Start event must be blank")   );

    }
    @Test
    public void superfluousGatewayValidator    (){
        assert SuperfluousGatewayValidator    .validate(loadDoc("test/superfluous-gateway-correct.bpmn")).isEmpty();
        List<Issue> issueList =SuperfluousGatewayValidator    .validate(loadDoc("test/superfluous-gateway-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Gateway_1",7, "Gateway is superfluous (1 incoming, 1 outgoing)")   );

    }
    @Test
    public void superfluousTerminationValidator    (){
        //assert SuperfluousTerminationValidator    .validate(loadDoc("test/superfluous-termination-correct.bpmn")).isEmpty();
        List<Issue> issueList =SuperfluousTerminationValidator    .validate(loadDoc("test/superfluous-termination-incorrect.bpmn"));

        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Event_06ykaze",19, "Termination is superfluous.")   );

    }

}
