package sq.bpmn.plugin;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XmlValidations {

    public static  boolean singleBlankStartEvent(Document document){
        Elements elements = document.select("bpmn|startEvent");
        int blankStarts=0;
        for (Element element : elements) {
            if(element.select("bpmn|eventDefinitions").isEmpty()){
                blankStarts++;
            }
        }
        if(blankStarts>1){
            return  false;
        }
        return  true;
    }
}
