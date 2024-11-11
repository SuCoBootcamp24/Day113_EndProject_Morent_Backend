package de.morent.backend.entities;

import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private String brand;

    private String model;

    private int seats;

    private int engineCapacity;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private boolean isAutomatic;

    private float consumption;

    @OneToOne(cascade = {CascadeType.ALL})
    private Image image = new Image();

    @OneToMany(mappedBy = "vehicle", cascade = {CascadeType.ALL})
    private List<Review> reviews;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id && seats == vehicle.seats && engineCapacity == vehicle.engineCapacity && isAutomatic == vehicle.isAutomatic && Float.compare(consumption, vehicle.consumption) == 0 && carType == vehicle.carType && Objects.equals(brand, vehicle.brand) && Objects.equals(model, vehicle.model) && fuelType == vehicle.fuelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carType, brand, model, seats, engineCapacity, fuelType, isAutomatic, consumption);
    }
}
