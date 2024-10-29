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
    @Column
    private CarType carType;

    @Column
    private String brand;

    @Column
    private String model;

    @Column
    private int seats;

    @Column
    private int engineCapacity;

    @Enumerated(EnumType.STRING)
    @Column
    private FuelType fuelType;

    @Column
    private boolean isAutomatic;

    @Column
    private float consumption;

    @OneToOne(cascade = {CascadeType.ALL})
    private Image image;

    @OneToMany(mappedBy = "vehicle", cascade = {CascadeType.ALL})
    private List<Review> reviews;

}
