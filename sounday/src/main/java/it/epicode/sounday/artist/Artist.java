package it.epicode.sounday.artist;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
public class Artist {
    @Id
    private Long id; // Same as User ID

    private String stageName;
    private LocalDateTime profileUpdated;

    // Getters and Setters
}