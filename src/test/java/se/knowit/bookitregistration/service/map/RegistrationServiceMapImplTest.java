package se.knowit.bookitregistration.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceMapImplTest {
    
    private final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
    private Map<Long, Registration> container;
    private RegistrationServiceMapImpl service;
    
    @BeforeEach
    void setUp() {
        this.container = new ConcurrentHashMap<>();
        this.service = new RegistrationServiceMapImpl(this.container);
    }
    
    @Test
    void anEmptyServiceShouldReturnAnEmptySetFromFindAll() {
        Set<Registration> registrations = service.findAll();
        assertNotNull(registrations);
        assertTrue(registrations.isEmpty());
    }
    
    @Test
    void allAvailableRegistrationsShouldBeReturnedFromFindAll() throws ConflictingEntityException {
        Registration registration = validRegistration();
        service.save(registration);
        Set<Registration> allRegistrations = service.findAll();
        assertTrue(allRegistrations.contains(registration));
        assertEquals(1, allRegistrations.size());
    }
    
    @Test
    void deleteRequestShouldRemoveTheRegistration() throws ConflictingEntityException {
        Registration registration = validRegistration();
        service.save(registration);
        assertEquals(1, container.size());
        service.delete(registration.getRegistrationId().toString());
        assertEquals(0, container.size());
    }
    
    @Test
    void testSaveAValidRegistrationToEmptyMap() throws ConflictingEntityException {
        Registration incomingRegistration = validRegistration();
        Registration savedRegistration = service.save(incomingRegistration);
        assertEquals(1L, savedRegistration.getId());
        assertTrue(uuidIsNotNullOrBlank(savedRegistration.getRegistrationId()));
    }
    
    @Test
    void testSaveAValidRegistrationToFilledMap() throws ConflictingEntityException {
        service.save(validRegistration());
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        incomingRegistration.setEmail("test@test.com");
        Registration savedRegistration = service.save(incomingRegistration);
        assertEquals(2L, savedRegistration.getId());
        assertTrue(uuidIsNotNullOrBlank(incomingRegistration.getRegistrationId()));
    }
    
    @Test
    void testSaveAnInvalidRegistrationShouldThrowException() {
        Registration incomingRegistration = validRegistration();
        incomingRegistration.setEmail("test");
        assertThrows(IllegalArgumentException.class, () -> service.save(incomingRegistration));
    }
    
    @Test
    void testSaveADuplicateRegistrationShouldThrowException() throws ConflictingEntityException {
        service.save(validRegistration());
        assertThrows(ConflictingEntityException.class, () -> service.save(validRegistration()));
    }
    
    private Registration validRegistration() {
        Registration registration = new Registration();
        registration.setEventId(DEFAULT_UUID);
        registration.setEmail("valid@email.com");
        return registration;
    }
    
    private boolean uuidIsNotNullOrBlank(UUID uuid) {
        return uuid != null && !uuid.toString().isBlank();
    }
    
}
