package com.bpmnlint;


import com.bpmnlint.validator.AdHocSubProcessValidator;
import com.bpmnlint.validator.ConditionalFlowsValidator;
import com.bpmnlint.validator.EndEventRequiredValidator;
import org.assertj.core.groups.Tuple;



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
        System.out.println(path);
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

}
