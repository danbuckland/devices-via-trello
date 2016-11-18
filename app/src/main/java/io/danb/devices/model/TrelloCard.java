package io.danb.devices.model;


import java.util.ArrayList;

public class TrelloCard {
    private String id;
    private String name;
    private ArrayList<TrelloLabel> labels;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<TrelloLabel> getLabels() {
        return labels;
    }

}
