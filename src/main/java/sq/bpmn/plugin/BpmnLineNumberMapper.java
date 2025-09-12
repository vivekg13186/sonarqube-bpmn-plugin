package sq.bpmn.plugin;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
public class BpmnLineNumberMapper {
    public static Map<String, Integer> mapElementLineNumbers(File file) {
        Map<String, Integer> elementLineMap = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(file)) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(fis);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String id = null;
                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                        if ("id".equals(reader.getAttributeLocalName(i))) {
                            id = reader.getAttributeValue(i);
                            break;
                        }
                    }
                    if (id != null) {
                        elementLineMap.put(id, reader.getLocation().getLineNumber());
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return elementLineMap;
    }
}
