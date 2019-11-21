package se.knowit.bookitregistration.repository;

import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Set;
import java.util.function.Predicate;

public interface RegistrationRepository {

    Set<Registration> find(Predicate<Registration> searchFilter);

    void delete(Predicate<Registration> searchFilter);

    Registration save(Registration registration) throws ConflictingEntityException;
}
