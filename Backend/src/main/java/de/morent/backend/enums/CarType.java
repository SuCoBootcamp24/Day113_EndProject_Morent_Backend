package de.morent.backend.enums;

public enum CarType {
    SPORT("Sport"),
    SUV("SUV"),
    MPV("MPV"),
    SEDAN("Sedan"),
    COUPE("Coupe"),
    HATCHBACK("Hatchback");

    private final String typeName;

    CarType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
