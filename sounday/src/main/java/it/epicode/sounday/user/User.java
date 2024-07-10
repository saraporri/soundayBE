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
    private Integer followersCount; //artist

    @OneToMany
    private List<Event> likeEvents;

    @OneToMany
    private List<User> likeArtists;


    @OneToMany
    private List<Event> events; //artist

    @OneToMany
    private List<Event> partecipation;







    // Getters and Setters
}