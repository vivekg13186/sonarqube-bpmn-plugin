package com.bpmnlint;

import java.util.List;

public class TestResult {

    private String file;
    private List<Issue> result;


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<Issue> getResult() {
        return result;
    }

    public void setResult(List<Issue> result) {
        this.result = result;
    }
}
