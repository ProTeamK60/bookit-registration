package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;

import java.util.Set;

public interface RegistrationService {

    Set<Registration> findAll();

    Registration save(Registration object);

    void delete(String id);
}
