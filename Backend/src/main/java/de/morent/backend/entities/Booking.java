package de.morent.backend.entities;

import de.morent.backend.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Data
public class Booking {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String bookingNumber;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleExemplar vehicle;

    @Column
    private LocalDate pickUpDate;

    @Column
    private LocalDate plannedDropOffDate;

    @ManyToOne
    private Store pickUpLocation;

    @ManyToOne
    private Store dropOffLocation;

    @Column
    private BigDecimal totalPrice;

    @Column
    @ColumnDefault("false")
    private Boolean dropOffDifferentStoreExtraCharge;

    @Column
    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    private Handover handover;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime updated;

    @PrePersist
    public void onCreate(){
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updated = LocalDateTime.now();
    }

    public int getTotalDays() {
        return (int) ChronoUnit.DAYS.between(pickUpDate, plannedDropOffDate);
    }
}
