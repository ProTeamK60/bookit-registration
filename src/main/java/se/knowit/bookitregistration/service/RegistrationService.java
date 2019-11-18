package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Set;

public interface RegistrationService {

    Set<Registration> findAll();

    Registration save(Registration object) throws ConflictingEntityException;

    void delete(String id);
}
