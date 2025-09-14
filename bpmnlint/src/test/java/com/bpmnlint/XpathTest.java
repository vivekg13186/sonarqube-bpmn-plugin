package com.bpmnlint;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;

public class XpathTest extends JFrame {

    JTextField filepath =new JTextField("test/conditional-flows-incorrect.bpmn");
    JTextField xpath =new JTextField("//bpmn:sequenceFlow[@sourceRef='ExclusiveGateway_13buf0z']");
    JTextArea textArea = new JTextArea();
    JTextArea xmlArea = new JTextArea();

    JButton eval = new JButton("Eval");
    public XpathTest(){
        Xpath xpath1 =new Xpath();
        setSize(600,600);
        JSplitPane splitPane =new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(textArea), new JScrollPane(xmlArea));

        JPanel panel = columns(10,filepath,xpath,eval,splitPane);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel,BorderLayout.CENTER);

        eval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    textArea.setText("");
                    Document document = DOMLineNumberParser.parseWithLineNumbers(RuleTest3.readFile(filepath.getText()));
                    xmlArea.setText(toXmlString(document));
                    textArea.append(xpath.getText()+"\n");
                    NodeList nodeList = xpath1.getNodeList(xpath.getText(),document);
                    for(int i=0;i<nodeList.getLength();i++){
                        textArea.append(nodeList.item(i).getNodeName()+Util.getAttr(nodeList.item(i),"id"));
                        textArea.append("\n=====================\n");
                    }
                    if(nodeList.getLength()==0){
                        textArea.append("NO result");
                    }
                } catch (Exception ex) {
                    textArea.setText(ex.getMessage());
                }

            }
        });
        setVisible(true);
    }
    public static JPanel columns(int spacing,JComponent... components){
        JPanel panel=  new JPanel();
        BoxLayout layout =new BoxLayout(panel,BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        for (JComponent component : components) {
            if (component instanceof JTextField) {
                component.setMaximumSize(component.getPreferredSize());
            }
            panel.add(component);
            component.setAlignmentY(Component.CENTER_ALIGNMENT);
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(Box.createRigidArea(new Dimension(0, spacing)));
        }

        return panel;
    }

    public static String toXmlString(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        // optional: make it pretty
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.toString();
    }
    public static void  main(String[] args){
        XpathTest xpathTest = new XpathTest();
    }
}
