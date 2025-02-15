package de.morent.backend.enums;

public enum BookingStatus {
    CANCELLED("Storniert"),
    CONFIRMED("Bestätigt"),
    COMPLETED("Abgeschlossen"),
    IN_REVIEW("In Revision");

    private final String statusName;

    BookingStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}