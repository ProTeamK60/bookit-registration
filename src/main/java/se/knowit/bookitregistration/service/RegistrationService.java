package se.knowit.bookitregistration.service;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Set;

public interface RegistrationService {

    Set<Registration> findAll();
    
    Set<Registration> findRegistrationsByEventId(String eventId);
    
    Registration save(Registration object) throws ConflictingEntityException;

    void deleteByRegistrationId(String id);

    void deleteByEventIdAndEmail(String eventId, String email);
}
