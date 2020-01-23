package se.knowit.bookitregistration.repository;

import se.knowit.bookitregistration.model.event.Event;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EventRepository {
    Set<Event> findAll();

    Optional<Event> findByEventId(UUID id);

    Optional<Event> findById(Long aLong);

    Event save(Event object);
}
