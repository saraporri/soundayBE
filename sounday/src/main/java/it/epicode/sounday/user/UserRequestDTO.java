package it.epicode.sounday.user;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String role;


    // Getters and Setters
}