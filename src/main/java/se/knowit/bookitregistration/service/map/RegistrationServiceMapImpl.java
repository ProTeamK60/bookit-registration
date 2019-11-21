package se.knowit.bookitregistration.service.map;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.model.RegistrationValidator;
import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class RegistrationServiceMapImpl implements RegistrationService {
    private final Map<Long, Registration> registrationStore;
    private IdentityHandler identityHandler;
    private RegistrationValidator registrationValidator;

    public RegistrationServiceMapImpl() {
        this(new ConcurrentHashMap<>());
    }

    RegistrationServiceMapImpl(Map<Long, Registration> registrationStore) {
        this.registrationStore = registrationStore;
        this.identityHandler = new IdentityHandler(this.registrationStore.size());
        this.registrationValidator = new RegistrationValidator();
    }

    @Override
    public Set<Registration> findAll() {
        return Set.copyOf(registrationStore.values());
    }

    @Override
    public Registration save(Registration incomingRegistration) throws ConflictingEntityException {
        Registration validRegistration = registrationValidator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
        assignRequiredIds(validRegistration);
        persistRegistration(validRegistration);
        return validRegistration;
    }

    @Override
    public void delete(String registrationId) {
        requireNonNull(registrationId, "registrationId can't be null");
        Optional<Registration> registrationToDelete = registrationStore.values()
                .stream()
                .filter(r -> r.getRegistrationId().toString().equals(registrationId))
                .findFirst();
        registrationToDelete.ifPresent(registration -> registrationStore.remove(registration.getId()));
    }

    @Override
    public void deleteByEventIdAndEmail(String eventId, String email) {
        requireNonNull(eventId, "eventId can't be null");
        requireNonNull(email, "email can't be null");

        Set<Registration> registrationsByEventId = findRegistrationsByEventId(eventId);
        Optional<Registration> registrationToDelete = registrationsByEventId.stream()
                .filter(r -> r.getParticipant().getEmail().equals(email))
                .findFirst();
        registrationToDelete.ifPresent(r -> delete(r.getRegistrationId().toString()));
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
            if (registration.getRegistrationId() == null) {
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

    @Override
    public Set<Registration> findRegistrationsByEventId(String eventId) {
        return findAll().stream()
                .filter(haveSameEventId(eventId))
                .collect(Collectors.toSet());
    }

    private Predicate<Registration> haveSameEventId(String eventId) {
        return registration -> registration.getEventId().equals(UUID.fromString(eventId));
    }
}
