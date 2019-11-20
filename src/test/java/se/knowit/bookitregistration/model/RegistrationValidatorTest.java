package se.knowit.bookitregistration.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegistrationValidatorTest {

    private final RegistrationValidator validator = new RegistrationValidator();
    private final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");

    @Test
    public void testValidatingAValidRegistration() {
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        Participant participant = new Participant("test@test.com");
        incomingRegistration.setParticipant(participant);
        Registration validRegistration = validator.ensureRegistrationIsValidOrThrowException(incomingRegistration);
        assertEquals(incomingRegistration, validRegistration);
    }

    @Test
    public void testValidatingRegistrationWithInvalidEmailShouldThrowException()
    {
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        Participant participant = new Participant("test");
        incomingRegistration.setParticipant(participant);
        assertThrows(IllegalArgumentException.class, () -> validator.ensureRegistrationIsValidOrThrowException(incomingRegistration));
    }

    @Test
    public void testValidatingRegistrationWithInvalidEventIdShouldThrowException()
    {
        Registration incomingRegistration = new Registration();
        Participant participant = new Participant("test@test.com");
        incomingRegistration.setParticipant(participant);
        assertThrows(IllegalArgumentException.class, () -> validator.ensureRegistrationIsValidOrThrowException(incomingRegistration));
    }

}
