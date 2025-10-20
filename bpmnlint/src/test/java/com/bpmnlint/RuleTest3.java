package com.bpmnlint;


import com.bpmnlint.validator.*;
import org.assertj.core.groups.Tuple;


import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleTest3 {

    private static Yaml yaml;

    interface  Callback{
        List<Issue> validate(Document document) throws Exception;
    }
    static  Map<String, Object> testData;
    @BeforeAll
    public static void beforeAll(){
        Yaml yaml = new Yaml();
        InputStream inputStream = RuleTest3.class
                .getClassLoader()
                .getResourceAsStream("testresult.yml");
        testData= yaml.load(inputStream);
    }

    public static List<Object> getResult(String path){
        HashMap<String,Object> file = (HashMap<String, Object>) testData.get(path);
        return (List<Object>) file.get("result") ;
    }

    public static void match(String path,Callback callback) throws Exception {
        //System.out.println(path);
        List<Object> records = getResult(path);
        Tuple[] tuples =new Tuple[records.size()];
        for(int i=0;i<records.size();i++){
            List<Object> rec = (List<Object>) records.get(i);
            tuples[i]=tuple(rec.get(0), rec.get(1), rec.get(2));
        }
        Document document =loadDoc(path);
        if(records.isEmpty()){
            List<Issue> issue = callback.validate(document);
            assertTrue(callback.validate(document).isEmpty());
        }else {
            assertThat(callback.validate(document)).extracting("id", "line", "message")
                    .contains(tuples);
        }
    }
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
    public static Document loadDoc(String path) throws Exception {
       // System.out.println(readFile(path));
        return DOMLineNumberParser.parseWithLineNumbers(readFile(path));
    }

    @Test
    void conditionalFlowsValidator() throws Exception {
        match("test/conditional-flows-correct.bpmn",(ConditionalFlowsValidator::validate));
        match("test/conditional-flows-incorrect.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/invalid-fork-after-exclusive-gateway.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/invalid-fork-after-exclusive-gateway-default.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/invalid-fork-after-task.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/invalid-fork-after-task-default.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/valid-conditional-fork.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/valid-no-condition-after-merge.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/valid-split.bpmn",(ConditionalFlowsValidator::validate));
        match("test/rules/conditional-flows/valid-split-after-task.bpmn",(ConditionalFlowsValidator::validate));
    }
    @Test
    void adHocSubProcessValidator() throws Exception {
        match("test/ad-hoc-sub-process-correct.bpmn",(AdHocSubProcessValidator::validate));
        match("test/ad-hoc-sub-process-incorrect.bpmn",(AdHocSubProcessValidator::validate));
        match("test/rules/ad-hoc-sub-process/valid.bpmn",(AdHocSubProcessValidator::validate));
        match("test/rules/ad-hoc-sub-process/invalid-start-end.bpmn",(AdHocSubProcessValidator::validate));
     }

     @Test
     void endEventRequiredValidator() throws Exception{
        match("test/end-event-required-correct.bpmn",(EndEventRequiredValidator::validate));
         match("test/end-event-required-incorrect.bpmn",(EndEventRequiredValidator::validate));
         match("test/rules/end-event-required/invalid.bpmn",(EndEventRequiredValidator::validate));
         match("test/rules/end-event-required/invalid-sub-process.bpmn",(EndEventRequiredValidator::validate));
         match("test/rules/end-event-required/invalid-sub-process-sub-types.bpmn",(EndEventRequiredValidator::validate));
         match("test/rules/end-event-required/valid.bpmn",(EndEventRequiredValidator::validate));
         match("test/rules/end-event-required/valid-sub-process.bpmn",(EndEventRequiredValidator::validate));
         match("test/rules/end-event-required/valid-sub-process-sub-types.bpmn",(EndEventRequiredValidator::validate));

     }

     @Test
     public void eventSubProcessTypedStartEventValidator() throws Exception{
         match("test/event-sub-process-typed-start-event-correct.bpmn",(EventSubProcessTypedStartEventValidator::validate));
         match("test/event-sub-process-typed-start-event-incorrect.bpmn",(EventSubProcessTypedStartEventValidator::validate));
         match("test/rules/event-sub-process-typed-start-event/invalid.bpmn",(EventSubProcessTypedStartEventValidator::validate));
         match("test/rules/event-sub-process-typed-start-event/valid.bpmn",(EventSubProcessTypedStartEventValidator::validate));
         match("test/rules/event-sub-process-typed-start-event/valid-empty.bpmn",(EventSubProcessTypedStartEventValidator::validate));
         match("test/rules/event-sub-process-typed-start-event/valid-empty-sub-process.bpmn",(EventSubProcessTypedStartEventValidator::validate));
         match("test/rules/event-sub-process-typed-start-event/valid-intermediate-event.bpmn",(EventSubProcessTypedStartEventValidator::validate));
         match("test/rules/event-sub-process-typed-start-event/valid-sub-process.bpmn",(EventSubProcessTypedStartEventValidator::validate));

     }

     /// TODO new tests below
    @Test
    public void fakeJoinValidator() throws Exception {
        match("test/fake-join-correct.bpmn",(FakeJoinValidator::validate));
        match("test/fake-join-incorrect.bpmn",(EventSubProcessTypedStartEventValidator::validate));
        match("test/rules/fake-join/invalid-callActivity.bpmn",(EventSubProcessTypedStartEventValidator::validate));
        match("test/rules/fake-join/invalid-task.bpmn",(EventSubProcessTypedStartEventValidator::validate));
        match("test/rules/fake-join/valid.bpmn",(EventSubProcessTypedStartEventValidator::validate));
        match("test/rules/fake-join/valid-gateway.bpmn",(EventSubProcessTypedStartEventValidator::validate));

    }

    @Test
    public void labelRequiredValidator() throws Exception{
        match("test/label-required-correct.bpmn",(LabelRequiredValidator::validate));
        match("test/label-required-incorrect.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/invalid-boundary-event.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/invalid-conditional-flow.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/invalid-event.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/invalid-gateway-split.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/invalid-lane.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/invalid-participant.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/invalid-task.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/valid-boundary-event.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/valid-conditional-flow.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/valid-data-objects.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/valid-gateways.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/valid-participant-lanes.bpmn",(LabelRequiredValidator::validate));
        match("test/rules/label-required/valid-start-event.bpmn",(LabelRequiredValidator::validate));


    }

    @Test
    public void linkEventValidator() throws Exception{
        match("test/link-event-correct.bpmn" ,(LinkEventValidator::validate));
        match("test/link-event-incorrect.bpmn",(LinkEventValidator::validate));
        match("test/rules/link-event/invalid.bpmn",(LinkEventValidator::validate));
        match("test/rules/link-event/valid.bpmn",(LinkEventValidator::validate));
        match("test/rules/link-event/valid-collaboration.bpmn",(LinkEventValidator::validate));

    }
    //TODO
    @org.junit.Test
    public void noBPMNDIValidator() throws Exception{
        match("test/no-bpmndi-correct.bpmn",NoBPMNDIValidator::validate);
        match("test/no-bpmndi-incorrect.bpmn",NoBPMNDIValidator::validate);
        match("ignore-edge-without-bpmn-element.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-catch-event.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-collapsed-pool.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-group.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-lane.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-message-flow.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-missing-nested-lane-deep.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-missing-nested-lane.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-multiple-nested-levels.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-multiple-sub-processes.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-nested-boundary.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-nested-lanes.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-no-bpmn-diagram.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-participant.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-sequence-flow.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/invalid-sub-processes.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-complex.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-data-object.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-empty.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-error.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-extension-elements.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-group.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-lanes.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-message-flow.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-multiple-nested-levels.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-nested-boundary.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-no-lanes.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-signavio.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid-sub-processes.bpmn",NoBPMNDIValidator::validate);
        match("test/rules/no-bpmndi/valid.bpmn",NoBPMNDIValidator::validate);

    }
    @org.junit.Test
    public void noComplexGatewayValidator (){
        assert NoComplexGatewayValidator .validate(loadDoc("test/no-complex-gateway-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoComplexGatewayValidator .validate(loadDoc("test/no-complex-gateway-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("ExclusiveGateway_2",29, "Use of <Complex Gateway> is discouraged") );
    }
    @org.junit.Test
    public void noDisconnectedValidator (){
        assert NoDisconnectedValidator .validate(loadDoc("test/no-disconnected-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoDisconnectedValidator .validate(loadDoc("test/no-disconnected-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_2",16, "Element <bpmn:task> is not connected to any sequence flow") );

    }

    @org.junit.Test
    public void noDuplicateSequenceFlowsValidator  (){
        assert NoDuplicateSequenceFlowsValidator  .validate(loadDoc("test/no-duplicate-sequence-flows-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoDuplicateSequenceFlowsValidator  .validate(loadDoc("test/no-duplicate-sequence-flows-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("SequenceFlow_4",23, "SequenceFlow is a duplicate") ,
                        tuple("Task_1",7, "Duplicate outgoing sequence flows") ,
                        tuple("Task_2",13, "Duplicate incoming sequence flows") );


    }

    @org.junit.Test
    public void noGatewayJoinForkValidator   (){
        assert NoGatewayJoinForkValidator   .validate(loadDoc("test/no-gateway-join-fork-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoGatewayJoinForkValidator   .validate(loadDoc("test/no-gateway-join-fork-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("ExclusiveGateway_1",10, "Gateway forks and joins simultaneously, which may lead to ambiguous logic")  );

    }

    @org.junit.Test
    public void noImplicitEndValidator  (){
        assert NoImplicitEndValidator    .validate(loadDoc("test/no-implicit-end-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoImplicitEndValidator    .validate(loadDoc("test/no-implicit-end-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_1",7, "Element is an implicit end (no outgoing sequence flow)")  );

    }

    @org.junit.Test
    public void noImplicitSplitValidator     (){
        assert NoImplicitSplitValidator     .validate(loadDoc("test/no-implicit-split-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoImplicitSplitValidator     .validate(loadDoc("test/no-implicit-split-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_1",7, "Element has multiple outgoing flows but is not a gateway—implicit split detected")  );

    }

    @org.junit.Test
    public void noImplicitStartValidator    (){
        assert NoImplicitStartValidator    .validate(loadDoc("test/no-implicit-start-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoImplicitStartValidator    .validate(loadDoc("test/no-implicit-start-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Task_1",4, "Element is an implicit start")  );

    }

    @org.junit.Test
    public void noInclusiveGatewayValidator    (){
        assert NoInclusiveGatewayValidator    .validate(loadDoc("test/no-inclusive-gateway-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoInclusiveGatewayValidator    .validate(loadDoc("test/no-inclusive-gateway-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("InclusiveGateway_1",24, "Inclusive gateways are discouraged")  ,
                        tuple("InclusiveGateway_2",29, "Inclusive gateways are discouraged")  );


    }

    @org.junit.Test
    public void noOverlappingElementsValidator    (){
        assert NoOverlappingElementsValidator    .validate(loadDoc("test/no-overlapping-elements-correct.bpmn")).isEmpty();
        List<Issue> issueList =NoOverlappingElementsValidator    .validate(loadDoc("test/no-overlapping-elements-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Activity_1v5k805",7, "Overlaps with element 'Activity_0g4fgz0'")  ,
                        tuple("Activity_0g4fgz0",4, "Overlaps with element 'Activity_1v5k805'")  );


    }

    @org.junit.Test
    public void singleBlankStartEventValidator    (){
        assert SingleBlankStartEventValidator    .validate(loadDoc("test/single-blank-start-event-correct.bpmn")).isEmpty();
        List<Issue> issueList =SingleBlankStartEventValidator    .validate(loadDoc("test/single-blank-start-event-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Process_1",3, "Process has multiple blank start events")   );

    }



    @org.junit.Test
    public void singleEventDefinitionValidator(){
        assert SingleEventDefinitionValidator    .validate(loadDoc("test/single-event-definition-correct.bpmn")).isEmpty();
        List<Issue> issueList =SingleEventDefinitionValidator    .validate(loadDoc("test/single-event-definition-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("IntermediateEvent_1",4, "Event has multiple event definitions")   );


    }

    @org.junit.Test
    public void startEventRequiredValidator(){
        assert StartEventRequiredValidator     .validate(loadDoc("test/start-event-required-correct.bpmn")).isEmpty();
        List<Issue> issueList =StartEventRequiredValidator     .validate(loadDoc("test/start-event-required-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Process_1",3, "Process is missing start event")   );

    }

    @org.junit.Test
    public void subProcessBlankStartEventValidator    (){
        assert SubProcessBlankStartEventValidator    .validate(loadDoc("test/sub-process-blank-start-event-correct.bpmn")).isEmpty();
        List<Issue> issueList =SubProcessBlankStartEventValidator    .validate(loadDoc("test/sub-process-blank-start-event-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("StartEvent_1",5, "Start event must be blank")   );

    }
    @org.junit.Test
    public void superfluousGatewayValidator    (){
        assert SuperfluousGatewayValidator    .validate(loadDoc("test/superfluous-gateway-correct.bpmn")).isEmpty();
        List<Issue> issueList =SuperfluousGatewayValidator    .validate(loadDoc("test/superfluous-gateway-incorrect.bpmn"));
        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Gateway_1",7, "Gateway is superfluous (1 incoming, 1 outgoing)")   );

    }
    @org.junit.Test
    public void superfluousTerminationValidator    (){
        //assert SuperfluousTerminationValidator    .validate(loadDoc("test/superfluous-termination-correct.bpmn")).isEmpty();
        List<Issue> issueList =SuperfluousTerminationValidator    .validate(loadDoc("test/superfluous-termination-incorrect.bpmn"));

        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuple("Event_06ykaze",19, "Termination is superfluous.")   );

    }

}
