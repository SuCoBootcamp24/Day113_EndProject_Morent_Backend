package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Vehicle vehicle;


    public Favorite() {
    }

    public Favorite(User user, Vehicle vehicle) {
        this.user = user;
        this.vehicle = vehicle;
    }
}
