package it.epicode.sounday.event;

import it.epicode.sounday.user.UserResponseDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class EventResponseDTO {
    private Long id;
    private String title;
    private String dateTime;
    private LocalDate eventDate;
    private String city;
    private String location;
    private UserResponseDTO artistId;
    //private Integer participantsCount;
   // private Integer likesCount;
    //private LocalDateTime createdDate;

    // Getters and Setters
}