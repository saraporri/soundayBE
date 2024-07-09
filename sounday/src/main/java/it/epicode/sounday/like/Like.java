package it.epicode.sounday.like;

import it.epicode.sounday.artist.Artist;
import it.epicode.sounday.event.Event;
import it.epicode.sounday.user.User;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;




@Entity
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = true)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = true)
    private Event event;

    private LocalDateTime likedDate;

    // Getters and Setters
}