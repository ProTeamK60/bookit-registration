package se.knowit.bookitregistration.repository.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.knowit.bookitregistration.RegistrationTestUtil.DEFAULT_UUID;
import static se.knowit.bookitregistration.RegistrationTestUtil.validRegistration;

class RegistrationRepositoryMapImplTest {

    private RegistrationRepositoryMapImpl repo;

    @BeforeEach
    void setUp() {
        this.repo = new RegistrationRepositoryMapImpl();
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
        Participant participant = new Participant("test@test.com");
        incomingRegistration.setParticipant(participant);
        Registration savedRegistration = repo.save(incomingRegistration);
        assertEquals(2L, savedRegistration.getId());
        assertTrue(uuidIsNotNullOrBlank(incomingRegistration.getRegistrationId()));
    }

    private boolean uuidIsNotNullOrBlank(UUID uuid) {
        return uuid != null && !uuid.toString().isBlank();
    }
}