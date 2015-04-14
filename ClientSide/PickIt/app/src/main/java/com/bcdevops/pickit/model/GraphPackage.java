package com.bcdevops.pickit.model;

import com.bcdevops.pickit.ChoiceActivity;

import java.util.HashMap;

public class GraphPackage {
    private ChoiceActivity.MyGraphview graph;
    private HashMap<String, Integer> colors;
    private String[] demoStrings;

    public GraphPackage(){}
    public GraphPackage(ChoiceActivity.MyGraphview graph, HashMap<String, Integer> colors, String[] demoStrings) {
        this.graph = graph;
        this.colors = colors;
        this.demoStrings = demoStrings;
    }
    public ChoiceActivity.MyGraphview getGraph() {
        return graph;
    }
    public HashMap<String, Integer> getColors() {
        return colors;
    }
    public String[] getDemoStrings() {
        return demoStrings;
    }
    public void setGraph(ChoiceActivity.MyGraphview graph) {
        this.graph = graph;
    }
    public void setColors(HashMap<String, Integer> colors) {
        this.colors = colors;
    }
    public void setDemoStrings(String[] demoStrings) {
        this.demoStrings = demoStrings;
    }
 }
