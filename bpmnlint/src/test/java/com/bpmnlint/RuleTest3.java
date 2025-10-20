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
             callback.validate(document);
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

    @Test
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


    @Test
    public void noComplexGatewayValidator () throws Exception {
        match("test/no-complex-gateway-correct.bpmn",NoComplexGatewayValidator::validate);
        match("test/no-complex-gateway-incorrect.bpmn",NoComplexGatewayValidator::validate);
        match("test/rules/no-complex-gateway/invalid.bpmn",NoComplexGatewayValidator::validate);
        match("test/rules/no-complex-gateway/valid.bpmn",NoComplexGatewayValidator::validate);
    }

    @Test
    public void noDisconnectedValidator ()throws Exception {
        match("test/no-disconnected-correct.bpmn",NoDisconnectedValidator::validate);
        match("test/no-disconnected-incorrect.bpmn",NoDisconnectedValidator::validate);
        match("test/rules/no-disconnected/valid.bpmn",NoDisconnectedValidator::validate);
        match("test/rules/no-disconnected/invalid.bpmn",NoDisconnectedValidator::validate);
        match("test/rules/no-disconnected/valid-adhoc-subprocess.bpmn",NoDisconnectedValidator::validate);
        match("test/rules/no-disconnected/valid-compensation.bpmn",NoDisconnectedValidator::validate);
        match("test/rules/no-disconnected/valid-event-subprocess.bpmn",NoDisconnectedValidator::validate);
        match("test/rules/no-disconnected/valid-text-annotation.bpmn",NoDisconnectedValidator::validate);


    }

    @Test
    public void noDuplicateSequenceFlowsValidator  ()throws Exception {
        match("test/no-duplicate-sequence-flows-correct.bpmn",NoDuplicateSequenceFlowsValidator::validate);
        match("test/no-duplicate-sequence-flows-incorrect.bpmn",NoDuplicateSequenceFlowsValidator::validate);
        match("test/rules/no-duplicate-sequence-flows/valid-adhoc-subprocess.bpmn",NoDuplicateSequenceFlowsValidator::validate);
        match("test/rules/no-duplicate-sequence-flows/invalid-condition.bpmn",NoDuplicateSequenceFlowsValidator::validate);
        match("test/rules/no-duplicate-sequence-flows/invalid-multiple.bpmn",NoDuplicateSequenceFlowsValidator::validate);
        match("test/rules/no-duplicate-sequence-flows/invalid-no-condition.bpmn",NoDuplicateSequenceFlowsValidator::validate);
        match("test/rules/no-duplicate-sequence-flows/valid.bpmn",NoDuplicateSequenceFlowsValidator::validate);
    }

    @Test
    public void noGatewayJoinForkValidator   ()throws Exception {
        match("test/no-gateway-join-fork-correct.bpmn",NoGatewayJoinForkValidator::validate);
        match("test/no-gateway-join-fork-incorrect.bpmn",NoGatewayJoinForkValidator::validate);
        match("test/rules/no-gateway-join-fork/valid-fork.bpmn",NoGatewayJoinForkValidator::validate);
        match("test/rules/no-gateway-join-fork/invalid.bpmn",NoGatewayJoinForkValidator::validate);
        match("test/rules/no-gateway-join-fork/valid-fork-join-task.bpmn",NoGatewayJoinForkValidator::validate);
        match("test/rules/no-gateway-join-fork/valid-join.bpmn",NoGatewayJoinForkValidator::validate);


    }

    @Test
    public void noImplicitEndValidator  ()throws Exception {
        match("test/no-implicit-end-correct.bpmn",NoImplicitEndValidator::validate);
        match("test/no-implicit-end-incorrect.bpmn",NoImplicitEndValidator::validate);
        match("test/rules/no-implicit-end/invalid.bpmn",NoImplicitEndValidator::validate);
        match("test/rules/no-implicit-end/valid.bpmn",NoImplicitEndValidator::validate);
        match("test/rules/no-implicit-end/valid-collaboration.bpmn",NoImplicitEndValidator::validate);

    }

    @Test
    public void noImplicitSplitValidator()throws Exception {
        match("test/no-implicit-split-correct.bpmn",NoImplicitSplitValidator::validate);
        match("test/no-implicit-split-incorrect.bpmn",NoImplicitSplitValidator::validate);
        match("test/rules/no-implicit-split/invalid-call-activity.bpmn",NoImplicitSplitValidator::validate);
        match("test/rules/no-implicit-split/invalid-event.bpmn",NoImplicitSplitValidator::validate);
        match("test/rules/no-implicit-split/invalid-task.bpmn",NoImplicitSplitValidator::validate);
        match("test/rules/no-implicit-split/valid.bpmn",NoImplicitSplitValidator::validate);
        match("test/rules/no-implicit-split/valid-default-conditional-flow.bpmn",NoImplicitSplitValidator::validate);
    }

    @Test
    public void noImplicitStartValidator    ()throws Exception {
        match("test/no-implicit-start-correct.bpmn",NoImplicitStartValidator::validate);
        match("test/no-implicit-start-incorrect.bpmn",NoImplicitStartValidator::validate);

        match("test/rules/no-implicit-start/invalid.bpmn",NoImplicitStartValidator::validate);
        match("test/rules/no-implicit-start/valid.bpmn",NoImplicitStartValidator::validate);
        match("test/rules/no-implicit-start/valid-collaboration.bpmn",NoImplicitStartValidator::validate);
    }

    @Test
    public void noInclusiveGatewayValidator    ()throws Exception {
        match("test/no-inclusive-gateway-correct.bpmn",NoInclusiveGatewayValidator::validate);
        match("test/no-inclusive-gateway-incorrect.bpmn",NoInclusiveGatewayValidator::validate);

        match("test/rules/no-inclusive-gateway/valid.bpmn",NoInclusiveGatewayValidator::validate);
        match("test/rules/no-inclusive-gateway/invalid.bpmn",NoInclusiveGatewayValidator::validate);
    }

    @Test
    public void noOverlappingElementsValidator    ()throws Exception {
        match("test/no-overlapping-elements-correct.bpmn",NoOverlappingElementsValidator::validate);
        match("test/no-overlapping-elements-incorrect.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/ignore-missing-bound.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/ignore-missing-di.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/invalid-boundary-event.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/invalid-collaboration.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/invalid-process.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/invalid-subprocess-collapsed.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/invalid-subprocess.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/valid-boundary-event.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/valid-collaboration-subprocess.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/valid-collaboration.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/valid-data-objects.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/valid-process.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/valid-subprocess-collapsed.bpmn",NoOverlappingElementsValidator::validate);
        match("test/rules/no-overlapping-elements/valid-subprocess.bpmn",NoOverlappingElementsValidator::validate);
    }

    @Test
    public void singleBlankStartEventValidator    ()throws Exception {
        match("test/single-blank-start-event-correct.bpmn",SingleBlankStartEventValidator::validate);
        match("test/single-blank-start-event-incorrect.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/invalid-sub-process.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/invalid.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/valid-empty.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/valid-end-event.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/valid-scopes.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/valid-sub-process.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/valid-typed-sub-process.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/valid-typed.bpmn",SingleBlankStartEventValidator::validate);
        match("test/rules/single-blank-start-event/valid.bpmn",SingleBlankStartEventValidator::validate);
    }



    @Test
    public void singleEventDefinitionValidator()throws Exception {
        match("test/single-event-definition-correct.bpmn",SingleEventDefinitionValidator::validate);
        match("test/single-event-definition-incorrect.bpmn",SingleEventDefinitionValidator::validate);
        match("test/rules/single-event-definition/invalid.bpmn",SingleEventDefinitionValidator::validate);
        match("test/rules/single-event-definition/valid-blank.bpmn",SingleEventDefinitionValidator::validate);
        match("test/rules/single-event-definition/valid.bpmn",SingleEventDefinitionValidator::validate);
    }

    @Test
    public void startEventRequiredValidator()throws Exception {
        match("test/start-event-required-correct.bpmn",StartEventRequiredValidator::validate);
        match("test/start-event-required-incorrect.bpmn",StartEventRequiredValidator::validate);
        match("test/rules/start-event-required/invalid-sub-process-sub-types.bpmn",StartEventRequiredValidator::validate);
        match("test/rules/start-event-required/invalid-sub-process.bpmn",StartEventRequiredValidator::validate);
        match("test/rules/start-event-required/invalid.bpmn",StartEventRequiredValidator::validate);
        match("test/rules/start-event-required/valid-sub-process-sub-types.bpmn",StartEventRequiredValidator::validate);
        match("test/rules/start-event-required/valid-sub-process.bpmn",StartEventRequiredValidator::validate);
        match("test/rules/start-event-required/valid.bpmn",StartEventRequiredValidator::validate);
    }

    @Test
    public void subProcessBlankStartEventValidator()throws Exception {
        match("test/sub-process-blank-start-event-correct.bpmn",SubProcessBlankStartEventValidator::validate);
        match("test/sub-process-blank-start-event-incorrect.bpmn",SubProcessBlankStartEventValidator::validate);
        match("test/rules/sub-process-blank-start-event/invalid-ad-hoc.bpmn",SubProcessBlankStartEventValidator::validate);
        match("test/rules/sub-process-blank-start-event/invalid.bpmn",SubProcessBlankStartEventValidator::validate);
        match("test/rules/sub-process-blank-start-event/valid-empty.bpmn",SubProcessBlankStartEventValidator::validate);
        match("test/rules/sub-process-blank-start-event/valid-event-sub-process.bpmn",SubProcessBlankStartEventValidator::validate);
        match("test/rules/sub-process-blank-start-event/valid-intermediate-event.bpmn",SubProcessBlankStartEventValidator::validate);
        match("test/rules/sub-process-blank-start-event/valid.bpmn",SubProcessBlankStartEventValidator::validate);
    }
    @Test
    public void superfluousGatewayValidator    ()throws Exception {
        match("test/superfluous-gateway-correct.bpmn",SuperfluousGatewayValidator::validate);
        match("test/superfluous-gateway-incorrect.bpmn",SuperfluousGatewayValidator::validate);
        match("test/rules/superfluous-gateway/invalid.bpmn",SuperfluousGatewayValidator::validate);
        match("test/rules/superfluous-gateway/valid-none-gateway.bpmn",SuperfluousGatewayValidator::validate);
        match("test/rules/superfluous-gateway/valid.bpmn",SuperfluousGatewayValidator::validate);

    }
    @Test
    public void superfluousTerminationValidator    ()throws Exception {
        match("test/superfluous-termination-correct.bpmn",SuperfluousTerminationValidator::validate);
        match("test/superfluous-termination-incorrect.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/invalid-boundary-interrupting.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/invalid-event-sub-process-interrupting.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/invalid-exclusive-paths.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/invalid-sub-process.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/invalid.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/valid-boundary-non-interrupting.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/valid-event-sub-process-non-interrupting.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/valid-implicit-end.bpmn",SuperfluousTerminationValidator::validate);
        match("test/rules/superfluous-termination/valid.bpmn",SuperfluousTerminationValidator::validate);



    }

}
