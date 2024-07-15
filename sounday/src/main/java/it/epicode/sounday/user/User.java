package it.epicode.sounday.user;

import it.epicode.sounday.event.Event;
import it.epicode.sounday.security.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    private final List<Roles> roles = new ArrayList<>();

    private Integer followersCount;

    @OneToMany(mappedBy = "artist")
    private List<Event> events;

    @OneToMany
    private List<Event> likeEvents = new ArrayList<>();

    @OneToMany
    private List<User> likeArtists = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_participate_event",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> partecipation = new ArrayList<>();

}
