package se.knowit.bookitregistration.model;

import java.util.Objects;

public class RegistrationValidator {

    public Registration ensureRegistrationIsValidOrThrowException(Registration incomingRegistration)
    {
        Registration registration = Objects.requireNonNull(incomingRegistration, "Registration must not be null.");
        ensureEmailIsValid(registration);
        ensureEventIdIsValid(registration);
        return registration;
    }

    private void ensureEventIdIsValid(Registration registration)
    {
        if(registration.getEventId() == null) throw new NullPointerException();
    }

    private void ensureEmailIsValid(Registration registration) {
        String email = registration.getEmail();
        if (email == null || email.isBlank() || !email.matches("^(.+)@(.+)$")) throw new IllegalArgumentException("Invalid email: " + email);
    }
}
