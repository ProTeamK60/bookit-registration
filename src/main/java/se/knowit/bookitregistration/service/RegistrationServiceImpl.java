package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.model.event.Event;
import se.knowit.bookitregistration.repository.EventRepository;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;
import se.knowit.bookitregistration.service.exception.MaxNumberOfRegistrationExceededException;
import se.knowit.bookitregistration.validator.RegistrationValidator;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final RegistrationValidator registrationValidator;
    
    public RegistrationServiceImpl(RegistrationRepository registrationRepository, EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.registrationValidator = new RegistrationValidator();
    }
    
    @Override
    public Set<Registration> findAll() {
        return registrationRepository.find(Predicates.matchAll);
    }
    
    @Override
    public Registration save(Registration incomingRegistration) throws ConflictingEntityException, MaxNumberOfRegistrationExceededException {
        Registration validRegistration = registrationValidator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
        eventRepository.findByEventId(validRegistration.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("The event you try to register for, does not exist in the backend!"));
        Optional<Event> e = eventRepository.findByEventId(validRegistration.getEventId());
        Event theEvent = e.get();
        if (null != theEvent.getMaxNumberOfApplicants()) {
          int maxNumberApplicants = theEvent.getMaxNumberOfApplicants();
          Set<Registration> numberOfRegistrations = registrationRepository.find(r -> r.getEventId().equals(validRegistration.getEventId()));
          if (numberOfRegistrations.size() >= maxNumberApplicants) {
            throw new MaxNumberOfRegistrationExceededException("Max number of registrations exceeded.");
          }
        }
        return registrationRepository.save(validRegistration);
         
    }
    
    @Override
    public void deleteByRegistrationId(String registrationId) {
        registrationRepository.delete(registrationIdMatcher(registrationId));
    }
    
    private Predicate<Registration> registrationIdMatcher(String registrationId) {
        return r -> r.getRegistrationId().toString().equals(registrationId);
    }
    
    @Override
    public void deleteByEventIdAndEmail(String eventId, String email) {
        registrationRepository.delete(eventIdAndEmailMatcher(eventId, email));
    }
    
    private Predicate<Registration> eventIdAndEmailMatcher(String eventId, String email) {
        return r -> r.getEventId().toString().equals(eventId) && r.getParticipant().getEmail().equals(email);
    }
    
    @Override
    public Set<Registration> findRegistrationsByEventId(String eventId) {
        return registrationRepository.find(eventIdMatcher(eventId));
    }
    
    private Predicate<Registration> eventIdMatcher(String eventId) {
        return r -> r.getEventId().equals(UUID.fromString(eventId));
    }

}
