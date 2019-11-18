package se.knowit.bookitregistration.service.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.RegistrationServiceMapImpl;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.UUID;

public class RegistrationServiceMapImplTest {

    private final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
    private RegistrationService service;

    @Before
    public void createRegistrationService()
    {
        this.service = new RegistrationServiceMapImpl();
    }

    @Test
    public void testSaveAValidRegistrationToEmptyMap() throws ConflictingEntityException {
        Registration incomingRegistration = validRegistration();
        Registration savedRegistration = service.save(incomingRegistration);
        Assert.assertEquals(1L, savedRegistration.getId(), 0);
        Assert.assertTrue(uuidIsNotNullOrBlank(savedRegistration.getRegistrationId()));
    }

    @Test
    public void testSaveAValidRegistrationToFilledMap() throws ConflictingEntityException {
        service.save(validRegistration());
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        incomingRegistration.setEmail("test@test.com");
        Registration savedRegistration = service.save(incomingRegistration);
        Assert.assertEquals(2L, savedRegistration.getId(), 0);
        Assert.assertTrue(uuidIsNotNullOrBlank(incomingRegistration.getRegistrationId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAnInvalidRegistrationShouldThrowException() throws ConflictingEntityException {
        Registration incomingRegistration = validRegistration();
        incomingRegistration.setEmail("test");
        service.save(incomingRegistration);
    }

    @Test(expected = ConflictingEntityException.class)
    public void testSaveADuplicateRegistrationShouldThrowException() throws ConflictingEntityException {
        service.save(validRegistration());
        service.save(validRegistration());
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
