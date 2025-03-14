package sq.bpmn.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import sq.bpmn.plugin.rules.*;

import java.io.*;

public class RuleTest {


    private String root = "C:\\Users\\Vivek\\Developer\\bpmnlint\\docs\\rules\\examples\\";
    private Document getDocument(String filename){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(root+filename));
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
    public void adHocSubProcessEventsTest() throws IOException {
        TestIssueMaker testIssueMaker = new TestIssueMaker();

        AdHocSubProcessEvents adHocSubProcessEvents = new AdHocSubProcessEvents();
        adHocSubProcessEvents.execute(null,getDocument("ad-hoc-sub-process-incorrect.bpmn"),null, null,testIssueMaker);
        assert (testIssueMaker.failed);
        testIssueMaker.failed=false;
        adHocSubProcessEvents.execute(null,getDocument("ad-hoc-sub-process-correct.bpmn"),null, null,testIssueMaker);
        assert (!testIssueMaker.failed);
    }


}
