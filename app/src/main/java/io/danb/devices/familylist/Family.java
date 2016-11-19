package io.danb.devices.familylist;

public class Family {

    private String familyName;

    private int availableDevices;
    private int assignedDevices;

    public Family(String familyName, int availableDevices, int assignedDevices) {
        this.familyName = familyName;
        this.availableDevices = availableDevices;
        this.assignedDevices = assignedDevices;
    }

    public String getName() {
        return familyName;
    }

    public int getAvailableDevices() {
        return availableDevices;
    }

    public int getAssignedDevices() {
        return assignedDevices;
    }

}
