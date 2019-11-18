package se.knowit.bookitregistration.service.map;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.model.RegistrationValidator;
import se.knowit.bookitregistration.service.RegistrationService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RegistrationServiceMapImpl implements RegistrationService {
    private final Map<Long, Registration> map;
    private IdentityHandler identityHandler;
    private RegistrationValidator registrationValidator;

    public RegistrationServiceMapImpl() {
        this(new ConcurrentHashMap<>());
    }

    RegistrationServiceMapImpl(Map<Long, Registration> map) {
        this.map = map;
        this.identityHandler = new IdentityHandler();
        this.registrationValidator = new RegistrationValidator();
    }

    @Override
    public Set<Registration> findAll() {
        return Set.copyOf(map.values());
    }

    @Override
    public Registration save(Registration incomingRegistration) {
        Registration validRegistration = registrationValidator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
        assignRequiredIds(validRegistration);
        persistRegistration(validRegistration);
        return validRegistration;
    }

    @Override
    public void delete(String id) {
        Optional<Registration> registrationToDelete = map.values()
                .stream()
                .filter(r -> r.getRegistrationId().toString().equals(id))
                .findFirst();

        registrationToDelete.ifPresent(registration -> map.remove(registration.getId()));
    }

    private void assignRequiredIds(Registration registration) {
        identityHandler.assignPersistenceIdIfNotSet(registration, this);
        identityHandler.assignRegistrationIdIfNotSet(registration);
    }

    private void persistRegistration(Registration registration) {
        map.put(registration.getId(), registration);
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
                return Collections.max(registrationServiceMap.map.keySet()) + 1L;
            } catch (NoSuchElementException e) {
                return 1L;
            }
        }
    }
}
