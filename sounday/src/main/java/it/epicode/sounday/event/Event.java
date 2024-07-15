package it.epicode.sounday.event;

import it.epicode.sounday.user.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
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

    private Integer participantsCount = 0; // Default to 0
    private Integer likesCount = 0; // Default to 0

    @ManyToMany(mappedBy = "likeEvents")
    private List<User> likedByUsers;

    // Getter and Setter for likesCount
    public Integer getLikesCount() {
        return likesCount;
    }
    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;
    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }  public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}
