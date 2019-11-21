package se.knowit.bookitregistration.repository.map;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class RegistrationRepositoryMapImpl implements RegistrationRepository {

    private IdentityHandler identityHandler;
    private final Map<Long, Registration> registrationStore;

    public RegistrationRepositoryMapImpl() {
        this(new ConcurrentHashMap<>());
    }

    public RegistrationRepositoryMapImpl(Map<Long, Registration> registrationStore) {
        this.registrationStore = registrationStore;
        this.identityHandler = new IdentityHandler(this.registrationStore.size());
    }

    @Override
    public Set<Registration> find(Predicate<Registration> searchFilter) {
        return registrationStore.values().stream().filter(searchFilter).collect(toSet());
    }

    @Override
    public void delete(Predicate<Registration> searchFilter) {
        Set<Long> idsOfRegistrationsToDelete = registrationStore.values()
                .stream()
                .filter(searchFilter)
                .map(Registration::getId)
                .collect(toUnmodifiableSet());

        idsOfRegistrationsToDelete.forEach(registrationStore::remove);
    }

    @Override
    public Registration save(Registration validRegistration) throws ConflictingEntityException {
        assignRequiredIds(validRegistration);
        persistRegistration(validRegistration);
        return validRegistration;
    }

    private void assignRequiredIds(Registration registration) {
        identityHandler.assignPersistenceIdIfNotSet(registration);
        identityHandler.assignRegistrationIdIfNotSet(registration);
    }

    private void persistRegistration(Registration registration) throws ConflictingEntityException {
        boolean alreadyRegistered = registrationStore.values()
                .stream()
                .filter(isSameEvent(registration))
                .anyMatch(isParticipantAlreadyRegistered(registration));
        if (alreadyRegistered) {
            throw supplyConflictException(registration).get();
        }
        registrationStore.putIfAbsent(registration.getId(), registration);
    }

    private Predicate<Registration> isSameEvent(Registration registration) {
        return r -> r.getEventId().toString().equals(registration.getEventId().toString());
    }

    private Predicate<Registration> isParticipantAlreadyRegistered(Registration registration) {
        return r -> r.getParticipant().getEmail().equals(registration.getParticipant().getEmail());
    }

    private Supplier<ConflictingEntityException> supplyConflictException(Registration registration) {
        String template = "The participant %s is already registered for this event.";
        String message = String.format(template, registration.getParticipant());
        return () -> new ConflictingEntityException(message);
    }

    private static class IdentityHandler {
        private final AtomicLong idValue;

        IdentityHandler(int startIndex) {
            idValue = new AtomicLong(startIndex);
        }

        void assignPersistenceIdIfNotSet(Registration registration) {
            if (registration.getId() == null) {
                registration.setId(getNextId());
            }
        }

        void assignRegistrationIdIfNotSet(Registration registration) {
            if (registration.getRegistrationId() == null) {
                registration.setRegistrationId(UUID.randomUUID());
            }
        }

        Long getNextId() {
            return idValue.incrementAndGet();
        }
    }
}
