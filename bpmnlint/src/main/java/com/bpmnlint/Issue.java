package com.bpmnlint;

import org.jsoup.nodes.Element;

public class Issue {

    private String id;
    private int line;
    private String message;

    public Issue(String id,int line, String message) {
        this.id = id;
        this.line = line;
        this.message = message;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Issue a){
        return  id.equals(a.id)&& message.equals(a.message)&&line ==a.line;
    }
}
