package com.bpmnlint;

import com.bpmn.model.TDefinitions;
import com.bpmnlint.validator.AdHocSubProcessValidator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.assertj.core.groups.Tuple;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RuleTest3 {

    public static File getResourceFile(String resourcePath) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = cl.getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return new File(resourceUrl.getFile());
    }
    public static TDefinitions fromFile(String fileName) throws JAXBException {
        File xmlFile = getResourceFile(fileName);

        // Create JAXB context for the generated classes
        JAXBContext jaxbContext = JAXBContext.newInstance(TDefinitions.class);

        // Create unmarshaller
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement<TDefinitions> rootElement = (JAXBElement<TDefinitions>) unmarshaller.unmarshal(xmlFile);
        return rootElement.getValue();
    }

    public void match(List<Issue> issueList, Tuple tuples  ){

        assertThat(issueList).extracting("id", "line", "message")
                .contains(tuples);
    }
    @Test
    public void adHocSubProcessValidator() throws JAXBException {
        assertTrue(AdHocSubProcessValidator.validate(fromFile("test/ad-hoc-sub-process-correct.bpmn")).isEmpty());
        assertTrue(AdHocSubProcessValidator.validate(fromFile("test/rules/ad-hoc-sub-process/valid.bpmn")).isEmpty());
        assertThat(AdHocSubProcessValidator.validate(fromFile("test/rules/ad-hoc-sub-process/invalid-start-end.bpmn")))
                .extracting("id", "line", "message")
                .contains(
                        tuple("SubProcess_0zz45ap", 1, "A <Start Event> is not allowed in <Ad Hoc Sub Process>"),
                        tuple("SubProcess_0zz45ap", 1, "An <End Event> is not allowed in <Ad Hoc Sub Process>"));
        assertThat(AdHocSubProcessValidator.validate(fromFile("test/ad-hoc-sub-process-incorrect.bpmn")))
                .extracting("id", "line", "message")
                .contains(
                        tuple("Activity_1yjcy2x", 1, "A <Start Event> is not allowed in <Ad Hoc Sub Process>"),
                        tuple("Activity_1yjcy2x", 1, "An <End Event> is not allowed in <Ad Hoc Sub Process>"));
    }
}
