package it.epicode.sounday.user;

import it.epicode.sounday.event.Event;
import it.epicode.sounday.event.EventRepository;
import it.epicode.sounday.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.NoSuchElementException;
import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder encoder;
    private final UserRepository usersRepository;
    private final EventRepository eventRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public Optional<LoginResponseDTO> login(String username, String password) {
        try {
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            a.getAuthorities();
            SecurityContextHolder.getContext().setAuthentication(a);
            var user = usersRepository.findOneByUsername(username).orElseThrow();
            var dto = LoginResponseDTO.builder()
                    .withUser(RegisteredUserDTO.builder()
                            .withId(user.getId())
                            .withFirstName(user.getFirstName())
                            .withLastName(user.getLastName())
                            .withEmail(user.getEmail())
                            .withRoles(user.getRoles())
                            .withUsername(user.getUsername())
                            .build())
                    .build();

            dto.setToken(jwt.generateToken(a));
            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }










    }

    public RegisteredUserDTO register(RegisterUserDTO register) {
        if (usersRepository.existsByUsername(register.getUsername())) {
            throw new EntityExistsException("User already exists");
        }
        if (usersRepository.existsByEmail(register.getEmail())) {
            throw new EntityExistsException("Email already registered");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_USER).orElseThrow();
        User user = new User();
        BeanUtils.copyProperties(register, user);
        user.setPassword(encoder.encode(register.getPassword()));
        user.getRoles().add(roles);
        usersRepository.save(user);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, response);
        response.setRoles(List.of(roles));
        return response;
    }

    public RegisteredUserDTO registerArtist(RegisterUserDTO register) {
        if (usersRepository.existsByUsername(register.getUsername())) {
            throw new EntityExistsException("User already exists");
        }
        if (usersRepository.existsByEmail(register.getEmail())) {
            throw new EntityExistsException("Email already registered");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_ADMIN).orElseThrow();
        User user = new User();
        BeanUtils.copyProperties(register, user);
        user.setPassword(encoder.encode(register.getPassword()));
        user.getRoles().add(roles);
        usersRepository.save(user);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, response);
        response.setRoles(List.of(roles));
        return response;
    }

    public long getMaxFileSizeInBytes() {
        String[] parts = maxFileSize.split("(?i)(?<=[0-9])(?=[a-z])");
        long size = Long.parseLong(parts[0]);
        String unit = parts[1].toUpperCase();
        switch (unit) {
            case "KB":
                size *= 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
        }
        return size;
    }

    public RegisteredUserDTO updateUser(Long id, RegisterUserDTO updateUserDTO) {
        User user = usersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        if (updateUserDTO.getFirstName() != null) user.setFirstName(updateUserDTO.getFirstName());
        if (updateUserDTO.getLastName() != null) user.setLastName(updateUserDTO.getLastName());
        if (updateUserDTO.getUsername() != null) user.setUsername(updateUserDTO.getUsername());
        if (updateUserDTO.getEmail() != null) user.setEmail(updateUserDTO.getEmail());
        if (updateUserDTO.getPassword() != null) user.setPassword(encoder.encode(updateUserDTO.getPassword()));
        usersRepository.save(user);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, response);
        response.setRoles(user.getRoles());
        return response;
    }

    public String deleteUserById(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        usersRepository.deleteById(id);
        return "User with id " + id + " deleted successfully";
    }

    public List<RegisteredUserDTO> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(user -> {
                    RegisteredUserDTO dto = new RegisteredUserDTO();
                    BeanUtils.copyProperties(user, dto);
                    dto.setRoles(user.getRoles());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public RegisteredUserDTO getUserById(Long id) {
        User user = usersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        RegisteredUserDTO dto = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setRoles(user.getRoles());
        return dto;
    }

    public List<RegisteredUserDTO> getAllArtists() {
        return usersRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getRoleType().equals("ARTIST")))
                .map(user -> {
                    RegisteredUserDTO dto = new RegisteredUserDTO();
                    BeanUtils.copyProperties(user, dto);
                    dto.setRoles(user.getRoles());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void likeEvent(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        event.setLikesCount(event.getLikesCount() + 1);
        eventRepository.save(event);

        user.getLikeEvents().add(event);
        usersRepository.save(user);
    }

    public void participateInEvent(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        event.setParticipantsCount(event.getParticipantsCount() + 1);
        eventRepository.save(event);

        user.getPartecipation().add(event);
        usersRepository.save(user);
    }

    public void likeArtist(Long userId, Long artistId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        User artist = usersRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));

        if (!user.getLikeArtists().contains(artist)) {
            user.getLikeArtists().add(artist);
            usersRepository.save(user);
        }
    }

    public List<UserResponseDTO> getLikedArtistsByUser(Long userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return user.getLikeArtists().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRoles().stream().map(Roles::getRoleType).findFirst().orElse(null))
                .build();
    }


    public List<User> searchArtists(String query) {
        return usersRepository.findByUsernameContainingAndRoles_roleType(query, "ARTIST");
    }
}
