package it.epicode.sounday.event;

import it.epicode.sounday.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private Long id;
    private String title;
    private LocalDate eventDate;
    private String dateTime;
    private String location;
    private String city;
    private UserResponseDTO artist; // Modifica per includere l'artista
    private Integer participantsCount;
    private Integer likedByUsers;

}
