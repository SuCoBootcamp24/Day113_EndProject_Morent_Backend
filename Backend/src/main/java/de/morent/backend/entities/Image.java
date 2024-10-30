package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String imagesName;

    private String imageUrl;

    private String thumbnailUrl;

    @UpdateTimestamp
    private LocalDate uploadDate;


}
