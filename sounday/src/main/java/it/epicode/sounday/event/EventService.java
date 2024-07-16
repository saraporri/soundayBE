package it.epicode.sounday.event;

import it.epicode.sounday.security.Roles;
import it.epicode.sounday.user.User;
import it.epicode.sounday.user.UserRepository;
import it.epicode.sounday.user.UserResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public List<EventResponseDTO> getAllEvents() {
        log.info("Fetching all events");
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::convertToEventResponseDTO).collect(Collectors.toList());
    }

    public Optional<EventResponseDTO> getEventById(Long id) {
        log.info("Fetching event with id: {}", id);
        return eventRepository.findById(id).map(this::convertToEventResponseDTO);
    }

    public EventResponseDTO createEvent(EventRequestDTO request) {
        log.info("Creating event with title: {}", request.getTitle());
        Event event = new Event();
        BeanUtils.copyProperties(request, event);

        User artist = userRepository.findById(request.getArtistId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + request.getArtistId()));
        event.setArtist(artist);

        eventRepository.save(event);

        log.info("Event created with id: {}", event.getId());
        return convertToEventResponseDTO(event);
    }

    public EventResponseDTO updateEvent(Long id, EventRequestDTO updateEventDTO) {
        log.info("Updating event with id: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        User artist = userRepository.findById(updateEventDTO.getArtistId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + updateEventDTO.getArtistId()));

        if (!artist.getRoles().stream().anyMatch(role -> role.getRoleType().equals("ARTIST"))) {
            log.warn("User does not have permission to update the event");
            throw new SecurityException("User does not have permission to update the event");
        }

        BeanUtils.copyProperties(updateEventDTO, event, "id");
        event.setArtist(artist);

        eventRepository.save(event);

        log.info("Event updated with id: {}", event.getId());
        return convertToEventResponseDTO(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        // Rimuovi tutte le relazioni di like per questo evento
        userRepository.findAll().forEach(user -> {
            user.getLikeEvents().removeIf(event -> event.getId().equals(eventId));
            userRepository.save(user);
        });

        // Rimuovi tutte le partecipazioni per questo evento
        userRepository.findAll().forEach(user -> {
            user.getPartecipation().removeIf(event -> event.getId().equals(eventId));
            userRepository.save(user);
        });

        // Ora elimina l'evento
        eventRepository.deleteById(eventId);
    }

    @Transactional
    public void likeEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        if (!event.getLikedByUsers().contains(user)) {
            event.getLikedByUsers().add(user);
            event.setLikesCount(Optional.ofNullable(event.getLikesCount()).orElse(0) + 1);
            user.getLikeEvents().add(event);
            userRepository.save(user);
            eventRepository.save(event);
        }
    }

    public List<EventResponseDTO> getLikedEventsByUser(Long userId) {
        log.info("Fetching liked events for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return user.getLikeEvents().stream()
                .map(this::convertToEventResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void participateEvent(Long userId, Long eventId) {
        log.info("User with id: {} participates in event with id: {}", userId, eventId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        if (!user.getPartecipation().contains(event)) {
            user.getPartecipation().add(event);
            event.getParticipants().add(user);
            event.setParticipantsCount(Optional.ofNullable(event.getParticipantsCount()).orElse(0) + 1);
            userRepository.save(user);
            eventRepository.save(event);
            log.info("User with id: {} participated in event with id: {}", userId, eventId);
        }
    }

    public List<EventResponseDTO> getParticipatedEventsByUser(Long userId) {
        log.info("Fetching participated events for user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return user.getPartecipation().stream()
                .map(this::convertToEventResponseDTO)
                .collect(Collectors.toList());
    }

    private EventResponseDTO convertToEventResponseDTO(Event event) {
        UserResponseDTO artistResponse = UserResponseDTO.builder()
                .id(event.getArtist().getId())
                .username(event.getArtist().getUsername())
                .email(event.getArtist().getEmail())
                .firstName(event.getArtist().getFirstName())
                .lastName(event.getArtist().getLastName())
                .role(event.getArtist().getRoles().stream().map(Roles::getRoleType).findFirst().orElse(null))
                .build();

        EventResponseDTO response = EventResponseDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .dateTime(event.getDateTime())
                .location(event.getLocation())
                .city(event.getCity())
                .artist(artistResponse)
                .participantsCount(event.getParticipantsCount())
                .likedByUsers(event.getLikedByUsers().size())
                .build();

        log.info("Converted Event to EventResponseDTO: {}", response);
        return response;


    }  public List<Event> searchEvents(String query) {
        return eventRepository.findByTitleContainingIgnoreCaseOrCityContainingIgnoreCase(query, query);
    }

}
