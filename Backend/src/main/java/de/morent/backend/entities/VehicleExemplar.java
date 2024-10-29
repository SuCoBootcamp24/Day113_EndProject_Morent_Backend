package de.morent.backend.entities;

import de.morent.backend.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class VehicleExemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Vehicle vehicle;

    @Column
    private BigDecimal pricePerDay;

    @Column
    private int mileage;

    @Column
    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;

    @Column
    private LocalDate created;

    @Column
    private LocalDate updated;

    @Column
    private int constYear;

    @OneToOne(cascade = {CascadeType.ALL})
    private DamageProfile damageProfile;

    @ManyToOne
    private Store store;
}
