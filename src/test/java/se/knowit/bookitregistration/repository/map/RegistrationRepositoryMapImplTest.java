package se.knowit.bookitregistration.repository.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static se.knowit.bookitregistration.RegistrationTestUtil.DEFAULT_UUID;
import static se.knowit.bookitregistration.RegistrationTestUtil.validRegistration;

class RegistrationRepositoryMapImplTest {
    private Map<Long, Registration> registrationStore;
    private RegistrationRepositoryMapImpl repo;
    
    @BeforeEach
    void setUp() {
        this.registrationStore = new ConcurrentHashMap<>();
        this.repo = new RegistrationRepositoryMapImpl(this.registrationStore);
    }
    
    @Test
    void testSaveAValidRegistrationToEmptyMap() throws ConflictingEntityException {
        Registration incomingRegistration = validRegistration();
        Registration savedRegistration = repo.save(incomingRegistration);
        assertEquals(1L, savedRegistration.getId());
        assertTrue(uuidIsNotNullOrBlank(savedRegistration.getRegistrationId()));
    }
    
    @Test
    void testSaveAValidRegistrationToFilledMap() throws ConflictingEntityException {
        repo.save(validRegistration());
        Registration incomingRegistration = new Registration();
        incomingRegistration.setEventId(DEFAULT_UUID);
        Participant participant = new Participant();
        participant.setEmail("test@test.com");
        incomingRegistration.setParticipant(participant);
        Registration savedRegistration = repo.save(incomingRegistration);
        assertEquals(2L, savedRegistration.getId());
        assertTrue(uuidIsNotNullOrBlank(incomingRegistration.getRegistrationId()));
    }
    
    private boolean uuidIsNotNullOrBlank(UUID uuid) {
        return uuid != null && !uuid.toString().isBlank();
    }
    
    @Test
    void itShouldNotBePossibleToRegisterAnEmailTwiceToTheSameEvent() throws ConflictingEntityException {
        repo.save(validRegistration());
        assertThrows(ConflictingEntityException.class, () -> repo.save(validRegistration()));
    }
    
    @Test
    void itMustBePossibleToFindARegistrationBasedOnItsRegistrationID() throws ConflictingEntityException {
        Registration saved = repo.save(validRegistration());
        Set<Registration> registrations = repo.find(getRegistrationIdPredicate(saved));
        assertNotNull(registrations);
        assertEquals(1, registrations.size());
        assertTrue(registrations.contains(saved));
    }
    
    private Predicate<Registration> getRegistrationIdPredicate(Registration saved) {
        return r -> r.getRegistrationId().equals(saved.getRegistrationId());
    }
    
    @Test
    void itMustBePossibleToFindARegistrationBasedOnEmailAndEventID() throws ConflictingEntityException {
        Registration saved = repo.save(validRegistration());
        Set<Registration> registrations = repo.find(getRegistrationByEmailAndIdPredicate(saved));
        assertNotNull(registrations);
        assertEquals(1, registrations.size());
        assertTrue(registrations.contains(saved));
    }
    
    private Predicate<Registration> getRegistrationByEmailAndIdPredicate(Registration saved) {
        return r -> r.getParticipant().getEmail().equals(saved.getParticipant().getEmail())
                && r.getEventId().equals(saved.getEventId());
    }
    
    @Test
    void itMustBePossibleToDeleteARegistrationByRegistrationId() throws ConflictingEntityException {
        Registration saved = repo.save(validRegistration());
        assertEquals(1, registrationStore.size());
        repo.delete(getRegistrationIdPredicate(saved));
        assertEquals(0, registrationStore.size());
    }
}
