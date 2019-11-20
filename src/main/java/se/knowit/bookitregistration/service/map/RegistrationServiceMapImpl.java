package se.knowit.bookitregistration.service.map;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.model.RegistrationValidator;
import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

public class RegistrationServiceMapImpl implements RegistrationService {
    private final Map<Long, Registration> registrationStore;
    private IdentityHandler identityHandler;
    private RegistrationValidator registrationValidator;

    public RegistrationServiceMapImpl() {
        this(new ConcurrentHashMap<>());
    }

    RegistrationServiceMapImpl(Map<Long, Registration> registrationStore) {
        this.registrationStore = registrationStore;
        this.identityHandler = new IdentityHandler();
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
        Optional<Registration> registrationToDelete = registrationStore.values()
                .stream()
                .filter(r -> r.getRegistrationId().toString().equals(registrationId))
                .findFirst();

        registrationToDelete.ifPresent(registration -> registrationStore.remove(registration.getId()));
    }

    private void assignRequiredIds(Registration registration) {
        identityHandler.assignPersistenceIdIfNotSet(registration, this);
        identityHandler.assignRegistrationIdIfNotSet(registration);
    }

    private void persistRegistration(Registration registration) throws ConflictingEntityException {
        Optional<Registration> registrationToAdd = registrationStore.values()
                .stream()
                .filter(r -> r.getEventId().toString().equals(registration.getEventId().toString()))
                .filter(r -> r.getParticipant().getEmail().equals(registration.getParticipant().getEmail()))
                .findFirst();
        if(registrationToAdd.isPresent()) {
            throw new ConflictingEntityException("The participant is already registered for this event.");
        }
        registrationStore.put(registration.getId(), registration);
    }

    private static class IdentityHandler {
        void assignPersistenceIdIfNotSet(Registration registration, RegistrationServiceMapImpl registrationServiceMap) {
            if (registration.getRegistrationId() == null) {
                registration.setId(getNextId(registrationServiceMap));
            }
        }

        void assignRegistrationIdIfNotSet(Registration registration) {
            if (registration.getRegistrationId() == null) {
                registration.setRegistrationId(UUID.randomUUID());
            }
        }

        Long getNextId(RegistrationServiceMapImpl registrationServiceMap) {
            try {
                return Collections.max(registrationServiceMap.registrationStore.keySet()) + 1L;
            } catch (NoSuchElementException e) {
                return 1L;
            }
        }
    }

	@Override
	public Set<Registration> findRegistrationsByEventId(String eventId) {
		return findAll().stream()
				.filter(registration -> registration.getEventId().equals(UUID.fromString(eventId)))
				.collect(Collectors.toSet());
	}
}
