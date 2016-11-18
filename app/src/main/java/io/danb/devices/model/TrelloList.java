package io.danb.devices.model;


import java.util.ArrayList;

public class TrelloList {

    private String id;
    private String name;
    private ArrayList<TrelloCard> cards;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<TrelloCard> getTrelloCards() {
        return cards;
    }

}
