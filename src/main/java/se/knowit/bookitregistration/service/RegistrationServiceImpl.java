package se.knowit.bookitregistration.service;

import org.springframework.kafka.annotation.KafkaListener;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;
import se.knowit.bookitregistration.validator.RegistrationValidator;

import java.util.*;
import java.util.function.Predicate;

public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository repository;
    private final RegistrationValidator registrationValidator;
    private final Set<String> existingEventIds = new HashSet<>();
    
    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
        this.registrationValidator = new RegistrationValidator();
    }
    
    @Override
    public Set<Registration> findAll() {
        return repository.find(Predicates.matchAll);
    }
    
    @Override
    public Registration save(Registration incomingRegistration) throws ConflictingEntityException {
        Registration validRegistration = registrationValidator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
        if(!existingEventIds.contains(validRegistration.getEventId().toString())) {
            throw new IllegalArgumentException("The event you try to register for, does not exist in the backend!");
        }

        return repository.save(validRegistration);
    }
    
    @Override
    public void deleteByRegistrationId(String registrationId) {
        repository.delete(registrationIdMatcher(registrationId));
    }
    
    private Predicate<Registration> registrationIdMatcher(String registrationId) {
        return r -> r.getRegistrationId().toString().equals(registrationId);
    }
    
    @Override
    public void deleteByEventIdAndEmail(String eventId, String email) {
        repository.delete(eventIdAndEmailMatcher(eventId, email));
    }
    
    private Predicate<Registration> eventIdAndEmailMatcher(String eventId, String email) {
        return r -> r.getEventId().toString().equals(eventId) && r.getParticipant().getEmail().equals(email);
    }
    
    @Override
    public Set<Registration> findRegistrationsByEventId(String eventId) {
        return repository.find(eventIdMatcher(eventId));
    }
    
    private Predicate<Registration> eventIdMatcher(String eventId) {
        return r -> r.getEventId().equals(UUID.fromString(eventId));
    }

    @KafkaListener(topics = {"events"}, containerFactory = "kafkaListenerContainerFactory")
    public void eventListener(final EventDTO event) {
        existingEventIds.add(event.getEventId());
        System.out.println("New incoming event " + event);
    }
}
