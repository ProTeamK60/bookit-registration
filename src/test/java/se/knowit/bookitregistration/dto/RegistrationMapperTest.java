package se.knowit.bookitregistration.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationMapperTest {
    
    private RegistrationMapper registrationMapper;
    
    @BeforeEach
    void setUp() {
        registrationMapper = new RegistrationMapper();
    }
    
    @Test
    void mapperShouldCopyAllFieldsFromDTOToRegistration() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        ParticipantDTO participant = new ParticipantDTO();
        participant.setEmail("test@test.com");
        registrationDTO.setParticipant(participant);
        registrationDTO.setEventId(UUID.randomUUID().toString());
        registrationDTO.setRegistrationId(UUID.randomUUID().toString());
        
        Registration registration = registrationMapper.fromDTO(registrationDTO);
        
        assertEquals(registrationDTO.getParticipant().getEmail(), registration.getParticipant().getEmail());
        assertEquals(registrationDTO.getEventId(), registration.getEventId().toString());
        assertEquals(registrationDTO.getRegistrationId(), registration.getRegistrationId().toString());
    }
}
