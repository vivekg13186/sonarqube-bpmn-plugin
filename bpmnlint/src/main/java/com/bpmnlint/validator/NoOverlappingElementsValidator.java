package com.bpmnlint.validator;


import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.bpmnlint.Util.*;

public class NoOverlappingElementsValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Map of element ID to its bounds
        Map<String, Rectangle> boundsMap = new HashMap<>();

        Elements shapes = doc.select("*|BPMNShape");

        for (Element shape : shapes) {
            String bpmnElement = shape.attr("bpmnElement");
            Element bounds = shape.selectFirst("*|Bounds");

            if (bounds != null) {
                double x = Double.parseDouble(bounds.attr("x"));
                double y = Double.parseDouble(bounds.attr("y"));
                double width = Double.parseDouble(bounds.attr("width"));
                double height = Double.parseDouble(bounds.attr("height"));

                boundsMap.put(bpmnElement, new Rectangle(x, y, width, height));
            }
        }

        // Compare each pair of elements for overlap
        List<String> ids = new ArrayList<>(boundsMap.keySet());
        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                String id1 = ids.get(i);
                String id2 = ids.get(j);

                Rectangle r1 = boundsMap.get(id1);
                Rectangle r2 = boundsMap.get(id2);

                if (r1.overlaps(r2)) {
                    Element el1 = doc.selectFirst("*[id=" + id1 + "]");
                    Element el2 = doc.selectFirst("*[id=" + id2 + "]");

                    if (el1 != null) {
                        result.add(issue(el1, "Overlaps with element '" + id2 + "'"));
                    }
                    if (el2 != null) {
                        result.add(issue(el2, "Overlaps with element '" + id1 + "'"));
                    }
                }
            }
        }

        return result;
    }

    // Helper class for rectangle comparison
    static class Rectangle {
        double x, y, width, height;

        Rectangle(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean overlaps(Rectangle other) {
            return x < other.x + other.width &&
                    x + width > other.x &&
                    y < other.y + other.height &&
                    y + height > other.y;
        }
    }
}