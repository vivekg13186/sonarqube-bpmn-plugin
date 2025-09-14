package com.bpmnlint;

import org.jsoup.nodes.Element;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        Issue a = (Issue) o;
        return line == a.line &&
                id.equals(a.id) &&
                message.equals(a.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, message);
    }
}
