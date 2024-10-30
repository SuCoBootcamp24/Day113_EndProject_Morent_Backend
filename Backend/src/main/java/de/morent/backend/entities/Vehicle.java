package de.morent.backend.entities;

import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
    private Image image;

    @OneToMany(mappedBy = "vehicle", cascade = {CascadeType.ALL})
    private List<Review> reviews;

}
