package it.epicode.sounday.event;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        Optional<EventResponseDTO> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO request, Authentication authentication) {
        if (!authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ARTIST"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EventResponseDTO response = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @RequestBody EventRequestDTO updateEventDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, updateEventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event with id " + id + " deleted successfully");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/{eventId}/like")
    public ResponseEntity<?> likeEvent(@RequestBody LikeRequest likeRequest, @PathVariable Long eventId) {
        try {
            log.info("User {} is liking event {}", likeRequest.getUserId(), eventId);
            eventService.likeEvent(likeRequest.getUserId(), eventId);
            log.info("Event {} liked successfully by user {}", eventId, likeRequest.getUserId());
            return ResponseEntity.ok().body("Event liked successfully");
        } catch (EntityNotFoundException e) {
            log.error("Error liking event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/liked")
    public ResponseEntity<List<EventResponseDTO>> getLikedEventsByUser(@RequestParam Long userId) {
        List<EventResponseDTO> likedEvents = eventService.getLikedEventsByUser(userId);
        return ResponseEntity.ok(likedEvents);
    }

    @PostMapping("/{eventId}/participate")
    public ResponseEntity<?> participateEvent(@RequestBody ParticipationRequest participationRequest, @PathVariable Long eventId) {
        try {
            log.info("User {} is participating in event {}", participationRequest.getUserId(), eventId);
            eventService.participateEvent(participationRequest.getUserId(), eventId);
            log.info("Event {} participated successfully by user {}", eventId, participationRequest.getUserId());
            return ResponseEntity.ok().body("Event participation confirmed successfully");
        } catch (EntityNotFoundException e) {
            log.error("Error participating in event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/participated")
    public ResponseEntity<List<EventResponseDTO>> getParticipatedEventsByUser(@RequestParam Long userId) {
        List<EventResponseDTO> participatedEvents = eventService.getParticipatedEventsByUser(userId);
        return ResponseEntity.ok(participatedEvents);
    }
}
