package se.knowit.bookitregistration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.knowit.bookitregistration.dto.ParticipantDTO;
import se.knowit.bookitregistration.service.ParticipantService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.knowit.bookitregistration.controller.ParticipantController.BASE_PATH;

@ExtendWith(MockitoExtension.class)
class ParticipantControllerTest {
    private static final UUID DEFAULT_EVENT_ID = UUID.randomUUID();
    @Mock
    private ParticipantService participantService;
    
    @InjectMocks
    private ParticipantController participantController;
    
    private MockMvc mockMvc;
    private ParticipantDTO participant1;
    private ParticipantDTO participant2;
    
    @BeforeEach
    void setUp() {
        
        participant1 = new ParticipantDTO();
        participant1.setEmail("ulf.lundell@knowit.se");
        
        participant2 = new ParticipantDTO();
        participant2.setEmail("lars.bandage@knowit.se");
        
        mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }
    
    @Test
    void allParticipantsOfAnEventShouldBeReturned() throws Exception {
        when(participantService
                .getParticipantsByEventId(
                        eq(DEFAULT_EVENT_ID.toString())))
                .thenReturn(Set.of(participant1, participant2));
        
        String jsonData = mockMvc.perform(get(BASE_PATH + "/event/" + DEFAULT_EVENT_ID.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        
        assertAllParticipantsAreReturned(extractParticipants(jsonData));
    }
    
    private Set<ParticipantDTO> extractParticipants(String jsonData) throws IOException {
        return new ObjectMapper().readValue(jsonData, new TypeReference<Set<ParticipantDTO>>() {});
    }
    
    private void assertAllParticipantsAreReturned(Set<ParticipantDTO> participantDTOS) {
        assertNotNull(participantDTOS);
        assertTrue(participantDTOS.contains(participant1));
        assertTrue(participantDTOS.contains(participant2));
    }
    
    @Test
    void requestForParticipantsShouldReturn_HTTP_NOT_FOUND_When_NoneHasRegistered() throws Exception {
        when(participantService
                .getParticipantsByEventId(
                        eq(DEFAULT_EVENT_ID.toString())))
                .thenReturn(Set.of());
        
        mockMvc.perform(get(BASE_PATH + "/event/" + DEFAULT_EVENT_ID.toString()))
                .andExpect(status().isNotFound());
    }
}
