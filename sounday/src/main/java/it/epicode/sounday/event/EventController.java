package it.epicode.sounday.event;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
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
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO request) {
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
    public ResponseEntity<?> likeEvent(@RequestParam Long userId, @PathVariable Long eventId) {
        try {
            eventService.likeEvent(userId, eventId);
            return ResponseEntity.ok().body("Event liked successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/liked")
    public ResponseEntity<List<EventResponseDTO>> getLikedEventsByUser(@RequestParam Long userId) {
        List<EventResponseDTO> likedEvents = eventService.getLikedEventsByUser(userId);
        return ResponseEntity.ok(likedEvents);
    }




    @PostMapping("/{eventId}/participate")
    public ResponseEntity<?> participateEvent(@RequestParam Long userId, @PathVariable Long eventId) {
        try {
            eventService.participateEvent(userId, eventId);
            return ResponseEntity.ok().body("Event participation confirmed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/participated")
    public ResponseEntity<List<EventResponseDTO>> getParticipatedEventsByUser(@RequestParam Long userId) {
        List<EventResponseDTO> participatedEvents = eventService.getParticipatedEventsByUser(userId);
        return ResponseEntity.ok(participatedEvents);
    }
}
