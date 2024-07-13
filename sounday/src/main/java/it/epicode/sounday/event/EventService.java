package it.epicode.sounday.event;

import it.epicode.sounday.user.User;
import it.epicode.sounday.user.UserRepository;
import it.epicode.sounday.user.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<EventResponseDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(event -> {
            EventResponseDTO response = new EventResponseDTO();
            BeanUtils.copyProperties(event, response);
            UserResponseDTO artistResponse = new UserResponseDTO();
            BeanUtils.copyProperties(event.getArtist(), artistResponse);
            response.setArtistId(artistResponse);
            return response;
        });
    }

    public EventResponseDTO createEvent(EventRequestDTO request) {
        Event event = new Event();
        BeanUtils.copyProperties(request, event);

        // Set the artist based on the artistId from the request
        User artist = userRepository.findById(request.getArtistId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + request.getArtistId()));
        event.setArtist(artist);

        eventRepository.save(event);

        EventResponseDTO response = new EventResponseDTO();
        BeanUtils.copyProperties(event, response);
        UserResponseDTO artistResponse = new UserResponseDTO();
        BeanUtils.copyProperties(artist, artistResponse);
        response.setArtistId(artistResponse);
        return response;
    }

    public EventResponseDTO updateEvent(Long id, EventRequestDTO updateEventDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        BeanUtils.copyProperties(updateEventDTO, event, "id");

        // Update the artist if provided
        if (updateEventDTO.getArtistId() != null) {
            User artist = userRepository.findById(updateEventDTO.getArtistId())
                    .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + updateEventDTO.getArtistId()));
            event.setArtist(artist);
        }

        eventRepository.save(event);

        EventResponseDTO response = new EventResponseDTO();
        BeanUtils.copyProperties(event, response);
        UserResponseDTO artistResponse = new UserResponseDTO();
        BeanUtils.copyProperties(event.getArtist(), artistResponse);
        response.setArtistId(artistResponse);
        return response;
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
