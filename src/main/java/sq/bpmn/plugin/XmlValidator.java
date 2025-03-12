package sq.bpmn.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import sq.bpmn.plugin.rules.SingleBlankStartEventRule;

public class XmlValidator {


    private static void createIssue(SensorContext context, RuleKey rulekey){
        NewIssue newIssue = context.newIssue();
        newIssue.forRule(rulekey)
                .save();
       // newIssue.newLocation().at(file.selectLine(lineNumber));
    }

    public static void validateXml(SensorContext context, String fileContent) {
        try {

            Document doc = Jsoup.parse(fileContent);


            if (!singleBlankStartEvent(doc)) {
                createIssue( context, RuleKey.of(Values.PLUGIN_REPO, SingleBlankStartEventRule.RULE_KEY));
            }

            if (!missingLabel(doc)) {
                System.err.println("Error: Missing name attributes in required elements");
            }

            if (!startEventRequired(doc)) {
                System.err.println("Error: Missing start event in Process or SubProcess");
            }

            if (!singleEventDefinition(doc)) {
                System.err.println("Error: An event has multiple eventDefinitions");
            }

            if (!adHocSubProcess(doc)) {
                System.err.println("Error: AdHocSubProcess contains invalid child elements");
            }


        } catch (Exception e) {
            System.err.println("Error reading XML file: " + e.getMessage());
        }
    }

    private static boolean validateRootElement(Document doc) {
        Element root = doc.child(0);
        return root != null && "root".equals(root.tagName()); // Change "root" to the actual expected root tag
    }

    private static boolean validateNamespaces(Document doc) {
        Elements elementsWithNamespace = doc.select("[xmlns]");
        return !elementsWithNamespace.isEmpty(); // Basic check for namespace presence
    }

    private static boolean validateRequiredElements(Document doc) {
        Elements requiredElements = doc.select("tagName"); // Replace "tagName" with required elements
        return !requiredElements.isEmpty();
    }

    private static boolean singleBlankStartEvent(Document doc) {
        Elements containers = doc.select("bpmn|flowElementContainer");
        for (Element container : containers) {
            Elements startEvents = container.select("bpmn|startEvent");
            int blankStartEventCount = 0;

            for (Element startEvent : startEvents) {
                if (startEvent.select("bpmn|eventDefinitions").isEmpty()) {
                    blankStartEventCount++;
                }
            }

            if (blankStartEventCount > 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean missingLabel(Document doc) {
        boolean isValid = true;

        Elements elementsToCheck = doc.select("bpmn|FlowNode, bpmn|Participant, bpmn|Lane");
        for (Element element : elementsToCheck) {
            if (!element.hasAttr("name")) {
                System.err.println("Error: Missing name attribute in " + element.tagName());
                isValid = false;
            }
        }

        Elements sequenceFlows = doc.select("bpmn|SequenceFlow");
        for (Element sequenceFlow : sequenceFlows) {
            if (!sequenceFlow.hasAttr("name") && !sequenceFlow.select("bpmn|conditionExpression").isEmpty()) {
                System.err.println("Error: Missing name attribute in SequenceFlow with conditionExpression");
                isValid = false;
            }
        }

        return isValid;
    }

    private static boolean startEventRequired(Document doc) {
        boolean isValid = true;

        Elements processes = doc.select("bpmn|Process, bpmn|SubProcess");
        for (Element process : processes) {
            if (process.select("bpmn|StartEvent").isEmpty()) {
                System.err.println("Error: Missing StartEvent in " + process.tagName());
                isValid = false;
            }
        }

        return isValid;
    }

    private static boolean singleEventDefinition(Document doc) {
        boolean isValid = true;

        Elements events = doc.select("bpmn|Event");
        for (Element event : events) {
            Elements eventDefinitions = event.select("bpmn|eventDefinitions");
            if (eventDefinitions.size() != 1) {
                System.err.println("Error: " + event.tagName() + " should have exactly one eventDefinitions child");
                isValid = false;
            }
        }

        return isValid;
    }

    private static boolean adHocSubProcess(Document doc) {
        boolean isValid = true;

        Elements adHocSubProcesses = doc.select("bpmn|AdHocSubProcess");
        for (Element adHocSubProcess : adHocSubProcesses) {
            if (!adHocSubProcess.select("bpmn|startEvent, bpmn|endEvent, bpmn|intermediateCatchEvent, bpmn|intermediateThrowEvent").isEmpty()) {
                System.err.println("Error: AdHocSubProcess contains invalid child elements");
                isValid = false;
            }
        }

        return isValid;
    }
}