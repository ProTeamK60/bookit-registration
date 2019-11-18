package se.knowit.bookitregistration.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class RegistrationValidatorTest {

    private final RegistrationValidator validator = new RegistrationValidator();
    private final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");

    @Test
    public void testValidatingAValidRegistration() {
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        incomingRegistration.setEmail("test@test.com");
        Registration validRegistration = validator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
        Assert.assertEquals(incomingRegistration, validRegistration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatingRegistrationWithInvalidEmailShouldThrowException()
    {
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        incomingRegistration.setEmail("test");
        validator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatingRegistrationWithInvalidEventIdShouldThrowException()
    {
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEmail("test@test.com");
        validator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
    }

}
