package se.knowit.bookitregistration.repository.map;

import se.knowit.bookitregistration.model.event.Event;
import se.knowit.bookitregistration.repository.EventRepository;
import se.knowit.bookitregistration.validator.EventValidator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventRepositoryMapImpl implements EventRepository {
    private final Map<Long, Event> map;
    private final EventValidator eventValidator;
    private final IdentityHandler identityHandler;

    public EventRepositoryMapImpl() {
        this(new ConcurrentHashMap<>());
    }

    EventRepositoryMapImpl(Map<Long, Event> map) {
        this.map = map;
        eventValidator = new EventValidator();
        identityHandler = new IdentityHandler();
    }

    @Override
    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Event save(Event incomingEvent) {
        Event event = eventValidator.ensureEventIsValidOrThrowException(incomingEvent);
        assignRequiredIds(event);
        persistEvent(event);
        return event;
    }


    private void assignRequiredIds(Event event) {
        identityHandler.assignPersistenceIdIfNotSet(event, this);
        identityHandler.assignEventIdIfNotSet(event);
    }

    private void persistEvent(Event event) {
        map.put(event.getId(), event);
    }


    @Override
    public Optional<Event> findByEventId(UUID id) {
        return map.values().stream()
                .filter(ev -> ev.haveEventId(id))
                .findFirst();
    }

    @Override
    public Set<Event> findAll() {
        return Set.copyOf(map.values());
    }

    private static class IdentityHandler {

        void assignPersistenceIdIfNotSet(Event event, EventRepositoryMapImpl eventRepositoryMap) {
            if (event.getId() == null) {
                event.setId(getNextId(eventRepositoryMap));
            }
        }

        void assignEventIdIfNotSet(Event event) {
            if (event.getEventId() == null) {
                event.setEventId(UUID.randomUUID());
            }
        }

        Long getNextId(EventRepositoryMapImpl eventRepositoryMap) {
            try {
                return Collections.max(eventRepositoryMap.map.keySet()) + 1L;
            } catch (NoSuchElementException ignored) {
                return 1L;
            }
        }
    }
}
