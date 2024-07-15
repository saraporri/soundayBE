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

    @ManyToMany(mappedBy = "partecipation")
    private List<User> participants;
    private Integer participantsCount = 0;

    @ManyToMany(mappedBy = "likeEvents")
    private List<User> likedByUsers;
    private Integer likesCount = 0; // Default to 0

    // Getter and Setter for likesCount
    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }


    public Integer getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }
}
