package de.morent.backend.enums;

public enum DamagePosition {
    FRONT_BUMPER("Frontstoßstange"),
    REAR_BUMPER("Heckstoßstange"),
    LEFT_DOOR("Linke Tür"),
    RIGHT_DOOR("Rechte Tür"),
    HOOD("Motorhaube"),
    ROOF("Dach"),
    TRUNK("Kofferraum"),
    LEFT_FENDER("Linker Kotflügel"),
    RIGHT_FENDER("Rechter Kotflügel"),
    WINDSHIELD("Windschutzscheibe"),
    REAR_WINDOW("Heckscheibe"),
    LEFT_HEADLIGHT("Linker Scheinwerfer"),
    RIGHT_HEADLIGHT("Rechter Scheinwerfer"),
    LEFT_TAILLIGHT("Linkes Rücklicht"),
    RIGHT_TAILLIGHT("Rechtes Rücklicht");

    private final String positionName;

    DamagePosition(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionName() {
        return positionName;
    }
}