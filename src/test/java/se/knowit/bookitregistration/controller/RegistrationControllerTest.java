package se.knowit.bookitregistration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.knowit.bookitregistration.dto.RegistrationDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    private static final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;

    private MockMvc mockMvc;

    private RegistrationMapper mapper = new RegistrationMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    void postRequest_WithValidData_ShouldReturn_201Created() throws Exception {
        String json = "{\"eventId\": \"" + UUID.randomUUID() + "\", \"email\": \"test@test.com\"}";
        RegistrationDTO dto = getRegistrationDTOFromJson(json);
        Registration savedRegistration = mapper.fromDTO(dto);
        savedRegistration.setRegistrationId(DEFAULT_UUID);

        when(registrationService.save(eq(savedRegistration))).thenReturn(savedRegistration);

        MvcResult result = mockMvc.perform(
                post("/api/v1/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
    }

    private RegistrationDTO getRegistrationDTOFromJson(String incomingJson) throws JsonProcessingException {
        return new ObjectMapper().readValue(incomingJson, RegistrationDTO.class);
    }
}