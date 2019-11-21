package se.knowit.bookitregistration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.knowit.bookitregistration.TestUtil.validRegistration;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    private RegistrationServiceImpl service;

    @Mock
    private RegistrationRepository repository;

    @BeforeEach
    void setUp() {
        this.service = new RegistrationServiceImpl(repository);
    }

    @Test
    void anEmptyServiceShouldReturnAnEmptySetFromFindAll() {
        Set<Registration> registrations = service.findAll();
        assertNotNull(registrations);
        assertTrue(registrations.isEmpty());
    }

    @Test
    void allAvailableRegistrationsShouldBeReturnedFromFindAll() throws ConflictingEntityException {
        Registration registration = validRegistration();
        when(repository.find(any())).thenReturn(Set.of(registration));

        service.save(registration);
        Set<Registration> allRegistrations = service.findAll();
        assertTrue(allRegistrations.contains(registration));
        assertEquals(1, allRegistrations.size());
    }

    @Test
    void deleteRequestShouldRemoveTheRegistration() throws ConflictingEntityException {
        Registration registration = validRegistration();

        when(repository.save(any())).thenReturn(registration);
        service.save(registration);
        verify(repository, times(1)).save(any());
        service.deleteByRegistrationId(registration.getRegistrationId().toString());
        verify(repository, times(1)).delete(any());
    }

    @Test
    void testSaveAnInvalidRegistrationShouldThrowException() {
        Registration incomingRegistration = validRegistration();
        Participant participant = new Participant("test");
        incomingRegistration.setParticipant(participant);
        assertThrows(IllegalArgumentException.class, () -> service.save(incomingRegistration));
    }

    @Test
    void testSaveADuplicateRegistrationShouldThrowException() throws ConflictingEntityException {
        service.save(validRegistration());
        when(repository.save(validRegistration())).thenThrow(new ConflictingEntityException());
        assertThrows(ConflictingEntityException.class, () -> service.save(validRegistration()));
    }
}
