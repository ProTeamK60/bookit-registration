package se.knowit.bookitregistration;

import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;

import java.util.UUID;

public class TestUtil {
    public static final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");

    public static Registration validRegistration() {
        Registration registration = new Registration();
        registration.setEventId(DEFAULT_UUID);
        registration.setRegistrationId(DEFAULT_UUID);
        Participant participant = new Participant("valid@email.com");
        registration.setParticipant(participant);
        return registration;
    }
}
