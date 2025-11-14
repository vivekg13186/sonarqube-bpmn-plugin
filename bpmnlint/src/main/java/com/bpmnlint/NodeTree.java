package com.bpmnlint;

import java.util.ArrayList;

public class NodeTree {

    public String name;

    public ArrayList<NodeTree> children = new ArrayList<>();

    private NodeTree(){

    }
    public boolean is(String match) {
        if (match.equals(name))
            return true;
        for (NodeTree c : children) {
            if (c.is(match))
                return true;
        }
        return false;
    }
    public final static NodeTree ROOT = new NodeTree();

    public static void build(){

    }
}