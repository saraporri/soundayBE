package it.epicode.sounday.user;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    private LocalDate registrationDate;

    // Getters and Setters
}