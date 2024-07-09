package it.epicode.sounday.user;

import it.epicode.sounday.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService user;

    @PostMapping("/register")
    public ResponseEntity<RegisteredUserDTO> register(@RequestBody @Validated RegisterUserModel model, BindingResult validator){
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        var registeredUser = user.register(
                RegisterUserDTO.builder()
                        .withFirstName(model.firstName())
                        .withLastName(model.lastName())
                        .withUsername(model.username())
                        .withEmail(model.email())
                        .withPassword(model.password())
                        .build());

        return  new ResponseEntity<> (registeredUser, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(user.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }

    @PostMapping("/registerArtist")
    public ResponseEntity<RegisteredUserDTO> registerArtist(@RequestBody RegisterUserDTO registerUser){
    return ResponseEntity.ok(user.registerAdmin(registerUser));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RegisteredUserDTO> updateUser(@PathVariable Long id, @RequestBody @Validated RegisterUserDTO updateUserDTO, BindingResult validator){
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        RegisteredUserDTO updatedUser = user.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            String result = user.deleteUserById(id);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping
    public ResponseEntity<List<RegisteredUserDTO>> getAllUsers() {
        List<RegisteredUserDTO> users = user.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUserDTO> getUserById(@PathVariable Long id) {
        try {
            RegisteredUserDTO users = user.getUserById(id);
            return ResponseEntity.ok(users);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
