package se.knowit.bookitregistration.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.RegistrationServiceMapImpl;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationServiceMapImplTest {

    private final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
    private RegistrationService service;

    @BeforeEach
    public void setUp()
    {
        this.service = new RegistrationServiceMapImpl();
    }

    @Test
    public void testSaveAValidRegistrationToEmptyMap() throws ConflictingEntityException {
        Registration incomingRegistration = validRegistration();
        Registration savedRegistration = service.save(incomingRegistration);
        assertEquals(1L, savedRegistration.getId());
        assertTrue(uuidIsNotNullOrBlank(savedRegistration.getRegistrationId()));
    }

    @Test
    public void testSaveAValidRegistrationToFilledMap() throws ConflictingEntityException {
        service.save(validRegistration());
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        incomingRegistration.setEmail("test@test.com");
        Registration savedRegistration = service.save(incomingRegistration);
        assertEquals(2L, savedRegistration.getId());
        assertTrue(uuidIsNotNullOrBlank(incomingRegistration.getRegistrationId()));
    }

    @Test
    public void testSaveAnInvalidRegistrationShouldThrowException() throws ConflictingEntityException {
        Registration incomingRegistration = validRegistration();
        incomingRegistration.setEmail("test");
        assertThrows(IllegalArgumentException.class, () -> service.save(incomingRegistration));
    }

    @Test
    public void testSaveADuplicateRegistrationShouldThrowException() throws ConflictingEntityException {
        service.save(validRegistration());
        assertThrows(ConflictingEntityException.class, () -> service.save(validRegistration()));
    }

    private Registration validRegistration()
    {
        Registration registration = new Registration();
        registration.setEventId(DEFAULT_UUID);
        registration.setEmail("valid@email.com");
        return registration;
    }

    private boolean uuidIsNotNullOrBlank(UUID uuid)
    {
        return uuid != null && !uuid.toString().isBlank();
    }

}
