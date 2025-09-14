package com.bpmnlint;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Iterator;

public class Xpath {
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xpath = xPathFactory.newXPath();
    public Xpath(){
        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if ("bpmn".equals(prefix)) {
                    return "http://www.omg.org/spec/BPMN/20100524/MODEL"; // BPMN default namespace
                }
                return XMLConstants.NULL_NS_URI;
            }

            @Override
            public String getPrefix(String namespaceURI) { return null; }

            @Override
            public Iterator getPrefixes(String namespaceURI) { return null; }
        });
    }
    public   NodeList getNodeList(String path, Node node) throws XPathExpressionException {
        return (NodeList) xpath.evaluate(path, node, XPathConstants.NODESET);
    }

    public boolean hasChild(String tagname,Node node) throws XPathExpressionException {
        return getNodeList(tagname,node).getLength()>0;
    }
    public boolean hasChild(String tagname,NodeList nodes) throws XPathExpressionException {
        for(int i=0;i<nodes.getLength();i++){
            if(hasChild(tagname,nodes.item(i))){
                return true;
            }
        }
        return false;
    }
}
