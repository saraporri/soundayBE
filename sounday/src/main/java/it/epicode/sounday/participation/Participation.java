package it.epicode.sounday.participation;





import it.epicode.sounday.event.Event;
import it.epicode.sounday.user.User;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private boolean confirmed;
    private LocalDateTime participationDate;

    // Getters and Setters
}