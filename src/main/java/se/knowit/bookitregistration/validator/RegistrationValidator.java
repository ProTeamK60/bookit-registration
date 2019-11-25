package se.knowit.bookitregistration.validator;

import se.knowit.bookitregistration.model.Registration;

import java.util.Objects;

public class RegistrationValidator {

    public Registration ensureRegistrationIsValidOrThrowException(Registration incomingRegistration) {
        Registration registration = Objects.requireNonNull(incomingRegistration, "Registration must not be null.");
        ensureParticipantIsValid(registration);
        ensureEventIdIsValid(registration);
        return registration;
    }

    private void ensureEventIdIsValid(Registration registration) {
        if (registration.getEventId() == null) {
            throw new IllegalArgumentException("Null EventId");
        }
    }

    private void ensureParticipantIsValid(Registration registration) {
        if (registration.getParticipant() == null) {
            throw new IllegalArgumentException("Null Participant Object");
        }

        String email = registration.getParticipant().getEmail();
        if (email == null || email.isBlank() || !email.matches("^(.+)@(.+)$")) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }
}
