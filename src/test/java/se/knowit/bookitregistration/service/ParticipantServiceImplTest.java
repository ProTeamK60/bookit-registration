package se.knowit.bookitregistration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.knowit.bookitregistration.dto.ParticipantDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceImplTest {
    private static final UUID EVENT_ID = UUID.fromString("a67f7b31-2985-46f1-8d20-5958649bf27a");
    @Mock
    private RegistrationService registrationService;
    @Mock
    private RegistrationMapper registrationMapper;
    @InjectMocks
    private ParticipantServiceImpl participantService;
    
    private ParticipantDTO firstParticipant;
    private ParticipantDTO secondParticipant;
    
    @BeforeEach
    void setUp() {
        Participant participant1 = new Participant("test1@test.com");
        firstParticipant = new ParticipantDTO();
        firstParticipant.setEmail(participant1.getEmail());
        
        Participant participant2 = new Participant("test2@test.com");
        secondParticipant = new ParticipantDTO();
        secondParticipant.setEmail(participant2.getEmail());
        
        RegistrationMapper regMapper = new RegistrationMapper();
        Registration registration1 = new Registration();
        registration1.setRegistrationId(UUID.fromString("00e964d5-103b-43c2-bb0c-c48b3fa02eca"));
        registration1.setEventId(EVENT_ID);
        registration1.setParticipant(participant1);
        
        Registration registration2 = new Registration();
        registration2.setRegistrationId(UUID.fromString("64e4834e-9df1-4fee-bb5a-3b69d15820d0"));
        registration2.setEventId(EVENT_ID);
        registration2.setParticipant(participant2);
        
        when(registrationMapper.toDTO(eq(registration1)))
                .thenReturn(regMapper.toDTO(registration1));
        when(registrationMapper.toDTO(eq(registration2)))
                .thenReturn(regMapper.toDTO(registration2));
        when(registrationService.findRegistrationsByEventId(eq(EVENT_ID.toString())))
                .thenReturn(Set.of(registration1, registration2));
    }
    
    @Test
    void allParticipantsForAnEventShouldBeReturnedFromFindByEventId() {
        Set<ParticipantDTO> participantsByEventId = participantService.getParticipantsByEventId(EVENT_ID.toString());
        assertNotNull(participantsByEventId);
        assertTrue(participantsByEventId.contains(firstParticipant));
        assertTrue(participantsByEventId.contains(secondParticipant));
        assertEquals(2, participantsByEventId.size());
    }
}
