package it.epicode.sounday.event;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EventRequestDTO {
    private String title;
    private LocalDateTime dateTime;
    private LocalDate eventDate;
    private String location;
    private Long artistId;
    private String city;
    // Getters and Setters
}