package se.knowit.bookitregistration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import se.knowit.bookitregistration.dto.RegistrationDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;

    private MockMvc mockMvc;

    private RegistrationMapper mapper = new RegistrationMapper();


    @Test
    void postRequest_WithValidData_ShouldReturn_201Created() throws Exception {
        String json = "{\"eventId\": \"" + UUID.randomUUID() + "\", \"email\": \"test@test.com\"}";
        RegistrationDTO dto = getRegistrationDtoOfJson(json);
        Registration registration = mapper.fromDTO(dto);
        when(registrationService.save(eq(registration))).thenReturn(registration);

        MvcResult result = mockMvc.perform(
                post("/api/v1/registrations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated())
                .andReturn();
    }

    private RegistrationDTO getRegistrationDtoOfJson(String incomingJson) throws JsonProcessingException {
        return new ObjectMapper().readValue(incomingJson, RegistrationDTO.class);
    }
}