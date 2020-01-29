package se.knowit.bookitregistration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.knowit.bookitregistration.RegistrationTestUtil.validRegistration;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    
    @InjectMocks
    private RegistrationServiceImpl service;
    
    @Mock
    private RegistrationRepository repository;
    
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
    void deleteRequestShouldRemoveTheRegistration() {
        Registration registration = validRegistration();
        service.deleteByRegistrationId(registration.getRegistrationId().toString());
        verify(repository).delete(notNull());
    }
    
    @Test
    void deleteByEmailAndEventIdShouldRemoveRegistration() {
        Registration registration = validRegistration();
        UUID eventId = registration.getEventId();
        String email = registration.getParticipant().getEmail();
        
        service.deleteByEventIdAndEmail(eventId.toString(), email);
        verify(repository).delete(notNull());
    }
    
    @Test
    void testSaveAnInvalidRegistrationShouldThrowException() {
        Registration incomingRegistration = validRegistration();
        Participant participant = new Participant();
        participant.setEmail("test");
        incomingRegistration.setParticipant(participant);
        assertThrows(IllegalArgumentException.class, () -> service.save(incomingRegistration));
    }
    
    @Test
    void testSaveADuplicateRegistrationShouldThrowException() throws ConflictingEntityException {
        service.save(validRegistration());
        when(repository.save(validRegistration())).thenThrow(new ConflictingEntityException());
        assertThrows(ConflictingEntityException.class, () -> service.save(validRegistration()));
    }
    
    @Test
    void testFindByEventId() {
        Registration registration = validRegistration();
        String eventId = registration.getEventId().toString();
        
        when(repository.find(isNotNull())).thenReturn(Set.of(registration));
        Set<Registration> registrationsByEventId = service.findRegistrationsByEventId(eventId);
        verify(repository).find(isNotNull());
        
        assertNotNull(registrationsByEventId);
        assertEquals(1, registrationsByEventId.size());
        assertTrue(registrationsByEventId.contains(registration));
    }
}
