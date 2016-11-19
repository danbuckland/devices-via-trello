package io.danb.devices.familylist;


import java.util.ArrayList;

import io.danb.devices.devices.Device;

public class Family {

    private String name;
    private ArrayList<Device> devices;

    public Family(ArrayList<Device> devices) {
        this.devices = devices;
        this.name = "iPhones";
    }

    public String getName() {
        return name;
    }

}
