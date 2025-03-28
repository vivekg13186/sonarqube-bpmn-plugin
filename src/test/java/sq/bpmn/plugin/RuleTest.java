package sq.bpmn.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import sq.bpmn.plugin.rules.*;

import java.io.*;
import java.util.Objects;

public class RuleTest {


    private Document getDocument(String filename){
        try{
            String root = "C:\\Users\\Vivek\\Developer\\bpmnlint\\docs\\rules\\examples\\";
            BufferedReader reader = new BufferedReader(new FileReader(root +filename));
            StringBuilder text  = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
            return Jsoup.parse(text.toString(), Parser.xmlParser().setTrackPosition(true));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;

    }

    @Test
    public void adHocSubProcessEventsTest() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        RuleAdHocSubProcessEvents ruleAdHocSubProcessEvents = new RuleAdHocSubProcessEvents();
        ruleAdHocSubProcessEvents.execute(null,getDocument("ad-hoc-sub-process-incorrect.bpmn"),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        ruleAdHocSubProcessEvents.execute(null,getDocument("ad-hoc-sub-process-correct.bpmn"),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }
    @Test
    public void conditionFlowTest() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        ConditionalFlow test = new ConditionalFlow();
        test.execute(null,getDocument("conditional-flows-incorrect.bpmn"),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        test.execute(null,getDocument("conditional-flows-correct.bpmn"),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }

    @Test
    public void endEventRequired() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        RuleEndEventRequired test = new RuleEndEventRequired();
        test.execute(null, Objects.requireNonNull(getDocument("end-event-required-incorrect.bpmn")),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        test.execute(null, Objects.requireNonNull(getDocument("end-event-required-correct.bpmn")),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }

    @Test
    public void eventSubProcessTypedStartEvent() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        RuleEventSubProcessTypedStartEvent test = new RuleEventSubProcessTypedStartEvent();
        test.execute(null, Objects.requireNonNull(getDocument("event-sub-process-typed-start-event-incorrect.bpmn")),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        test.execute(null, Objects.requireNonNull(getDocument("event-sub-process-typed-start-event-correct.bpmn")),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }
    @Test
    public void fakeJoints() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        RuleFakeJoins test = new RuleFakeJoins();
        test.execute(null, Objects.requireNonNull(getDocument("fake-join-incorrect.bpmn")),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        test.execute(null, Objects.requireNonNull(getDocument("fake-join-correct.bpmn")),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }


    @Test
    public void noComplexJoints() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        RuleNoComplexGateway test = new RuleNoComplexGateway();
        test.execute(null, Objects.requireNonNull(getDocument("no-complex-gateway-incorrect.bpmn")),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        test.execute(null, Objects.requireNonNull(getDocument("no-complex-gateway-correct.bpmn")),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }

    @Test
    public void singleEventDef() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        RuleSingleEventDefinition test = new RuleSingleEventDefinition();
        test.execute(null, Objects.requireNonNull(getDocument("single-event-definition-incorrect.bpmn")),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        test.execute(null, Objects.requireNonNull(getDocument("single-event-definition-correct.bpmn")),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }

    @Test
    public void superfluousGateway() {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        RuleSuperfluousGateway test = new RuleSuperfluousGateway();
        test.execute(null, Objects.requireNonNull(getDocument("superfluous-gateway-incorrect.bpmn")),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        test.execute(null, Objects.requireNonNull(getDocument("superfluous-gateway-correct.bpmn")),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }


}
