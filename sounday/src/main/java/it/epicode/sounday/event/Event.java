package it.epicode.sounday.event;





import it.epicode.sounday.user.User;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate eventDate;
    private LocalDateTime dateTime;
    private String location;
    private String city;
    //tourPic

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private User artist;

    private Integer participantsCount;
    private Integer likesCount;

    // Getters and Setters
}