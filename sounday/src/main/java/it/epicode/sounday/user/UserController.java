package it.epicode.sounday.user;

import it.epicode.sounday.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService user;

    @PostMapping("/register")
    public ResponseEntity<RegisteredUserDTO> register(@RequestBody @Validated RegisterUserModel model, BindingResult validator) {
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

        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(user.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }

    @PostMapping("/registerArtist")
    public ResponseEntity<RegisteredUserDTO> registerArtist(@RequestBody RegisterUserDTO registerUser) {
        return ResponseEntity.ok(user.registerArtist(registerUser));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RegisteredUserDTO> updateUser(@PathVariable Long id, @RequestBody @Validated RegisterUserDTO updateUserDTO, BindingResult validator) {
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

    @GetMapping("/artists")
    public ResponseEntity<List<RegisteredUserDTO>> getAllArtists() {
        List<RegisteredUserDTO> artists = user.getAllArtists();
        return ResponseEntity.ok(artists);
    }

    @PostMapping("/likeEvent/{userId}/{eventId}")
    public ResponseEntity<?> likeEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            user.likeEvent(userId, eventId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event liked successfully");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/participateEvent/{userId}/{eventId}")
    public ResponseEntity<?> participateInEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            user.participateInEvent(userId, eventId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Participation in event successful");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/likeArtist/{userId}/{artistId}")
    public ResponseEntity<?> likeArtist(@PathVariable Long userId, @PathVariable Long artistId) {
        try {
            user.likeArtist(userId, artistId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Artist liked successfully");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/{userId}/likedArtists")
    public ResponseEntity<List<UserResponseDTO>> getLikedArtistsByUser(@PathVariable Long userId) {
        List<UserResponseDTO> likedArtists = user.getLikedArtistsByUser(userId);
        return ResponseEntity.ok(likedArtists);
    }
}
