package it.epicode.sounday.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTitleContainingIgnoreCaseOrCityContainingIgnoreCase(String title, String city);





}
