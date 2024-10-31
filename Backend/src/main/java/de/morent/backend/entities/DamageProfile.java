package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DamageProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "damageProfile")
    private List<Damage> damages = new ArrayList<>();

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime update;

    @PrePersist
    public void onCreate(){
        this.created = LocalDateTime.now();
        this.update = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.update = LocalDateTime.now();
    }
}
