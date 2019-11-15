package se.knowit.bookitregistration.service.map;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistrationServiceMapImpl implements RegistrationService {
    private final Map<Long, Registration> map;

    public RegistrationServiceMapImpl() { this(new ConcurrentHashMap<>());
    }

    RegistrationServiceMapImpl(Map<Long, Registration> map) {
        this.map = map;
    }

    @Override
    public Registration save(Registration registration) {
        persistRegistration(registration);
        return registration;
    }

    private void persistRegistration(Registration registration) {
        map.put(registration.getId(), registration);
    }
}
