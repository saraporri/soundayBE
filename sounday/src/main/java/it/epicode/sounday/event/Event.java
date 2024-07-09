package it.epicode.sounday.event;





import it.epicode.sounday.user.User;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private User artist;

    private Integer participantsCount;
    private Integer likesCount;
    private LocalDateTime createdDate;

    // Getters and Setters
}