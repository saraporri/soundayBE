package it.epicode.sounday.event;

import it.epicode.sounday.user.User;
import it.epicode.sounday.user.UserRepository;
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

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
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
        response.setArtistId(artist.getId());
        return response;
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}