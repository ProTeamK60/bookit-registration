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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    public static final String PATH = "/api/v1/registrations";
    private static final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

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
        savedRegistration.setId(1L);

        when(registrationService.save(eq(mapper.fromDTO(dto)))).thenReturn(savedRegistration);

        MvcResult result = mockMvc.perform(
                post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void postRequest_WithInvalidData_ShouldReturn_400() throws Exception {

        when(registrationService.save(any())).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(
                post(PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"badRequest\" : \"true\"}"))
                .andExpect(status().isBadRequest());
    }

    private RegistrationDTO getRegistrationDTOFromJson(String incomingJson) throws JsonProcessingException {
        return new ObjectMapper().readValue(incomingJson, RegistrationDTO.class);
    }
}