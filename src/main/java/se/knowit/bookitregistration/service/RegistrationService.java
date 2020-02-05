package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;
import se.knowit.bookitregistration.service.exception.MaxNumberOfRegistrationExceededException;

import java.util.Set;

public interface RegistrationService {

    Set<Registration> findAll();
    
    Set<Registration> findRegistrationsByEventId(String eventId);
    
    Registration save(Registration object) throws ConflictingEntityException, MaxNumberOfRegistrationExceededException;

    void deleteByRegistrationId(String id);

    void deleteByEventIdAndEmail(String eventId, String email);
}
