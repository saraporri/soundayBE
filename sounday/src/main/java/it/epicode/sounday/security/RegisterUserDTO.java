package it.epicode.sounday.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RegisterUserDTO {
    String firstName;
    String lastName;
    String username;
    String email;
    String password;
}