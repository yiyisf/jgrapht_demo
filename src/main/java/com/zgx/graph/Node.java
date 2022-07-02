package com.zgx.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;

    private List<List<Node>> data = new ArrayList<>();

    public Node(int id) {
        this.id = String.valueOf(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<Node>> getData() {
        return data;
    }

    public void setData(List<List<Node>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return id;
    }
}
