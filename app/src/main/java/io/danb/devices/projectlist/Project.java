package io.danb.devices.projectlist;


import java.util.ArrayList;

import io.danb.devices.model.TrelloCard;
import io.danb.devices.model.TrelloLabel;
import io.danb.devices.model.TrelloList;

public class Project {

    private String name;
    private int noOfiPhones;
    private TrelloList trelloList;

    public Project(TrelloList trelloList) {
        this.trelloList = trelloList;
        this.name = trelloList.getName();
        this.noOfiPhones = getNoOfiPhones();
    }

    public String getName() {
        return name;
    }

    public int getNoOfiPhones() {
        // Count the number of array items with the label "iPhone"
        ArrayList<TrelloCard> trelloCards = trelloList.getTrelloCards();
        int count = 0;
        for (TrelloCard trelloCard : trelloCards) {
            ArrayList<TrelloLabel> trelloLabels = trelloCard.getLabels();
            if (trelloLabels.size() > 0) {
                for (TrelloLabel trelloLabel : trelloLabels) {
                    if (trelloLabel.getName().equals("iPhone")) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public int getNoOfiPads() {
        return 0;
    }

    public int getNoOfAndroids() {
        return 0;
    }


    public int getNoOfOthers() {
        return 0;
    }
}
