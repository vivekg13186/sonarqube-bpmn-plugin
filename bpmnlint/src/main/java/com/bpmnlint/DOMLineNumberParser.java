package com.bpmnlint;

import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.StringReader;
import java.util.Stack;

public class DOMLineNumberParser {

    public static Document parseWithLineNumbers(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // optional but usually good
        DocumentBuilder builder = factory.newDocumentBuilder();

        try (StringReader reader = new StringReader(xmlContent)) {
            InputSource inputSource = new InputSource(reader);
            return builder.parse(inputSource);
        }

    }
}
