package it.epicode.sounday.event;

import java.time.LocalDateTime;

public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private Long artistId;
    private Integer participantsCount;
    private Integer likesCount;
    private LocalDateTime createdDate;

    // Getters and Setters
}