package it.epicode.sounday.event;

import it.epicode.sounday.user.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate eventDate;
    private String dateTime;
    private String location;
    private String city;

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private User artist;

    private Integer participantsCount;
    private Integer likesCount;
}
