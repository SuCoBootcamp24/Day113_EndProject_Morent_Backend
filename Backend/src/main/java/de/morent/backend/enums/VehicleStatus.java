package de.morent.backend.enums;

public enum VehicleStatus {
    EXCELLENT("Hervorragend"),
    GOOD("Gut"),
    POOR("Schlecht");

    private final String statusName;

    VehicleStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}