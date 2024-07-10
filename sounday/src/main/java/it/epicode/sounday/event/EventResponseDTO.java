package it.epicode.sounday.event;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class EventResponseDTO {
    private Long id;
    private String title;
    private LocalDateTime dateTime;
    private LocalDate eventDate;

    private String location;
    private Long artistId;
    //private Integer participantsCount;
   // private Integer likesCount;
    //private LocalDateTime createdDate;

    // Getters and Setters
}