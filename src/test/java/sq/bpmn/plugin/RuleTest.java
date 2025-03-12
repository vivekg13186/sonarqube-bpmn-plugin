package sq.bpmn.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import sq.bpmn.plugin.rules.SingleBlankStartEventRule;
import sq.bpmn.plugin.rules.StartEventRequiredRule;

import java.io.File;
import java.io.IOException;

public class RuleTest {


    @Test
    public void singleBlankStartEventTest() throws IOException {
        String file ="C:\\Users\\vivek\\Developer\\bpmnlint\\docs\\rules\\examples\\single-blank-start-event-incorrect.bpmn";
        Document doc = Jsoup.parse(new File(file), "utf-8");
        assert (!SingleBlankStartEventRule.validate(doc));
        file ="C:\\Users\\vivek\\Developer\\bpmnlint\\docs\\rules\\examples\\single-blank-start-event-correct.bpmn";
        doc = Jsoup.parse(new File(file), "utf-8");
        assert (SingleBlankStartEventRule.validate(doc));
    }

    @Test
    public void startEventRequiredTest() throws IOException {
        String file ="C:\\Users\\vivek\\Developer\\bpmnlint\\docs\\rules\\examples\\start-event-required-incorrect.bpmn";
        Document doc = Jsoup.parse(new File(file), "utf-8");
        assert (!StartEventRequiredRule.validate(doc));
        file ="C:\\Users\\vivek\\Developer\\bpmnlint\\docs\\rules\\examples\\start-event-required-correct.bpmn";
        doc = Jsoup.parse(new File(file), "utf-8");
        assert (StartEventRequiredRule.validate(doc));
    }
}
