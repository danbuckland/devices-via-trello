package io.danb.devices.devices;


import io.danb.devices.model.TrelloCard;

public class Device {

    private String name;

    public Device(TrelloCard trelloCard) {
        this.name = trelloCard.getName();
    }

    public String getName() {
        return name;
    }

}
