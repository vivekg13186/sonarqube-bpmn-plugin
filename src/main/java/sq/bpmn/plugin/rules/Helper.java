package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;

public class Helper {


    public static final String startEvent ="*|startEvent";
    public static final String endEvent ="*|startEvent";
    public static final String adHocSubProcess ="*|adHocSubProcess";
    public static final String intermediateCatchEvent ="*|intermediateCatchEvent";
    public static final String intermediateThrowEvent ="*|intermediateThrowEvent";
public static  final String eventDefinitions = "*|eventDefinitions";
    public static  final String outgoing = "*|outgoing";
    public static void setLocation(InputFile file, Element element, NewIssueLocation location,String message){
        location.at(file.newRange(
                element.sourceRange().start().lineNumber(),element.sourceRange().start().columnNumber(),
                element.sourceRange().end().lineNumber(),element.sourceRange().end().columnNumber()
        ));
        location.message(message);
    }

    public static Elements getAllGatewayElements(Document doc){
       String selector="*|complexGateway,*|exclusiveGateway,*|inclusiveGateway,*|parallelGateway,*|eventBasedGateway";
       return doc.select(selector);
    }
}
