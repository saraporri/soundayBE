package it.epicode.sounday.event;

import it.epicode.sounday.user.UserResponseDTO;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EventResponseDTO {
    private Long id;
    private String title;
    private LocalDate eventDate;
    private String dateTime;
    private String location;
    private String city;
    private UserResponseDTO artist; // Modifica per includere l'artista
    private Integer participantsCount;
    private Integer likesCount;
}
