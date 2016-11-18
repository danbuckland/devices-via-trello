package io.danb.devices.projectlist;


import java.util.ArrayList;

import io.danb.devices.model.TrelloCard;
import io.danb.devices.model.TrelloLabel;
import io.danb.devices.model.TrelloList;

public class Project {

    private String name;
    private int noOfiPhones;
    private int noOfiPads;
    private int noOfAndroids;
    private int noOfOthers;
    private TrelloList trelloList;

    public Project(TrelloList trelloList) {
        this.trelloList = trelloList;
        this.name = trelloList.getName();
        countDevices();
    }

    public String getName() {
        return name;
    }

    public void countDevices() {
        // Count the number of array items with the label "iPhone"
        ArrayList<TrelloCard> trelloCards = trelloList.getTrelloCards();
        int noOfiPhones = 0;
        int noOfiPads = 0;
        int noOfAndroids = 0;
        int noOfOthers = 0;
        for (TrelloCard trelloCard : trelloCards) {
            ArrayList<TrelloLabel> trelloLabels = trelloCard.getLabels();
            if (trelloLabels.size() > 0) {
                for (TrelloLabel trelloLabel : trelloLabels) {
                    switch (trelloLabel.getName()) {
                        case "iPhone": noOfiPhones++;
                            break;
                        case "iPad": noOfiPads++;
                            break;
                        case "Android": noOfAndroids++;
                            break;
                        case "Other": noOfOthers++;
                    }
                }
            }
        }
        this.noOfiPhones = noOfiPhones;
        this.noOfiPads = noOfiPads;
        this.noOfAndroids = noOfAndroids;
        this.noOfOthers = noOfOthers;
    }

    public int getNoOfiPhones() {
        return noOfiPhones;
    }

    public int getNoOfiPads() {
        return noOfiPads;
    }

    public int getNoOfAndroids() {
        return noOfAndroids;
    }

    public int getNoOfOthers() {
        return noOfOthers;
    }

}
