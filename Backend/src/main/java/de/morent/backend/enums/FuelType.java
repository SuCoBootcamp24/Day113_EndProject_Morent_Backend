package de.morent.backend.enums;

public enum FuelType {
    GASOLINE("Benzin"),
    DIESEL("Diesel"),
    ELECTRIC("Electric"),
    HYBRID("Hybrid");

    private final String typeName;

    FuelType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}