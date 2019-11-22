package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;
import se.knowit.bookitregistration.validator.RegistrationValidator;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository repository;
    private final RegistrationValidator registrationValidator;

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
        return repository.save(validRegistration);
    }

    @Override
    public void deleteByRegistrationId(String registrationId) {
        repository.delete(registrationIdMatcher(registrationId));
    }

    @Override
    public void deleteByEventIdAndEmail(String eventId, String email) {
        repository.delete(eventIdAndEmailMatcher(eventId, email));
    }

    @Override
    public Set<Registration> findRegistrationsByEventId(String eventId) {
        return repository.find(eventIdMatcher(eventId));
    }

    private Predicate<Registration> eventIdMatcher(String eventId) {
        return r -> r.getEventId().equals(UUID.fromString(eventId));
    }

    private Predicate<Registration> registrationIdMatcher(String registrationId) {
        return r -> r.getRegistrationId().toString().equals(registrationId);
    }

    private Predicate<Registration> eventIdAndEmailMatcher(String eventId, String email) {
        return r -> r.getEventId().toString().equals(eventId) && r.getParticipant().getEmail().equals(email);
    }
}
