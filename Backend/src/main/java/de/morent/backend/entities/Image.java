package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String imageUrl;

    @Column
    private String thumbnailUrl;

    @Column
    private LocalDate uploadDate;

    @PrePersist
    @PreUpdate
    private void onUpload(){
        this.uploadDate = LocalDate.now();
    }

}
