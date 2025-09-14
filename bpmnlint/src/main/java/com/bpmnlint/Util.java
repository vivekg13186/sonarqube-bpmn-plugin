package com.bpmnlint;

import org.jsoup.nodes.Element;
import org.w3c.dom.Node;

public class Util {



    public static Issue issue(Element element, String message) {
        return new Issue(element.id(), element.sourceRange().start().lineNumber(), message);
    }




    public static int getLineNumber(Node node){
       return 1;
    }
    public static String getAttr(Node node,String name){
        Node attr=node.getAttributes().getNamedItem(name);
        if(attr!=null){
            String value =attr.getNodeValue();
            return value!=null ?value :"";
        }
        return "";
    }
    public static boolean hasAttr(Node node,String name){
        Node attr=node.getAttributes().getNamedItem(name);
        return attr != null;
    }



}
