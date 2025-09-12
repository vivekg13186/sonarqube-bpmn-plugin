package com.bpmnlint;

import org.jsoup.nodes.Element;

public class Util {
    public static Issue issue(Element element, String message){
        return new Issue( element.id(),element.sourceRange().start().lineNumber(),message);
    }
}
