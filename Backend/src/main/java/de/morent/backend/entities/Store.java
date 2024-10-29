package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;

    @OneToMany(mappedBy = "store", cascade = {CascadeType.ALL})
    private List<VehicleExemplar> vehicleExemplars;

}
