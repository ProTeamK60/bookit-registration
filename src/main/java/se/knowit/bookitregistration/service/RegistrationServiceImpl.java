package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.model.RegistrationValidator;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class RegistrationServiceImpl implements RegistrationService {
    private RegistrationRepository repository;
    private RegistrationValidator registrationValidator;

    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
        this.registrationValidator = new RegistrationValidator();
    }

    @Override
    public Set<Registration> findAll() {
        return repository.find(Filters.matchAll);
    }

    @Override
    public Registration save(Registration incomingRegistration) throws ConflictingEntityException {
        Registration validRegistration = registrationValidator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
        return repository.save(validRegistration);
    }

    @Override
    public void deleteByRegistrationId(String registrationId) {
        repository.delete(r -> r.getRegistrationId().toString().equals(registrationId));
    }

    @Override
    public Set<Registration> findRegistrationsByEventId(String eventId) {
        return repository.find(hasSameEventId(eventId));
    }

    private Predicate<Registration> hasSameEventId(String eventId) {
        return registration -> registration.getEventId().equals(UUID.fromString(eventId));
    }
}
