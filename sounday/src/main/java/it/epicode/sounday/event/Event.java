package it.epicode.sounday.event;

import it.epicode.sounday.user.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private Integer participantsCount = 0;
    private Integer likesCount = 0;

    @ManyToMany(mappedBy = "likeEvents", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> likedByUsers = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants = new ArrayList<>();

    public Event() {
        this.participantsCount = 0; // Assicura che participantsCount sia sempre inizializzato a 0
        this.likesCount = 0; // Assicura che likesCount sia sempre inizializzato a 0
    }
}
