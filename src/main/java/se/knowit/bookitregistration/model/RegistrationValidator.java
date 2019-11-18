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
        if(registration.getEventId() == null) throw new IllegalArgumentException();
    }

    private void ensureEmailIsValid(Registration registration)
    {
        ensureIsNotNullOrBlank(registration.getEmail());
    }

    private void ensureIsNotNullOrBlank(String value)
    {
        if(value == null || value.isBlank()) throw new IllegalArgumentException();
    }
}
