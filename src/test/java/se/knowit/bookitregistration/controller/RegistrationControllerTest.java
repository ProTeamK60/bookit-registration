package se.knowit.bookitregistration.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.knowit.bookitregistration.dto.RegistrationDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    private static final String PATH = "/api/v1/registrations";
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
    void requestForAllRegistrationsShouldReturn_404NotFound_WhenNoRegistrationsAreAvailable() throws Exception {
        when(registrationService.findAll()).thenReturn(Set.of());
        mockMvc.perform(get(PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void requestForAllRegistrationsShouldReturnJsonWithAllRegistrations() throws Exception {
        String json = "{\"eventId\": \"" + UUID.randomUUID() + "\", \"email\": \"test@test.com\"}";
        RegistrationDTO dto = getRegistrationDTOFromJson(json);
        Registration savedRegistration = mapper.fromDTO(dto);
        savedRegistration.setRegistrationId(DEFAULT_UUID);
        savedRegistration.setId(1L);
        
        when(registrationService.findAll()).thenReturn(Set.of(savedRegistration));
        String returnedJson = mockMvc.perform(get(PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<RegistrationDTO> registrationsFromJson = getRegistrationsFromJson(returnedJson);
        Registration returnedRegistration = mapper.fromDTO(registrationsFromJson.get(0));
        returnedRegistration.setId(savedRegistration.getId()); //Not part of the dto
        assertEquals(savedRegistration, returnedRegistration);
    }
    
    @Test
    void postRequest_WithValidData_ShouldReturn_201Created() throws Exception {
        String json = "{\"eventId\": \"" + UUID.randomUUID() + "\", \"participant\": {\"email\": \"test@test.com\"}}";
       
        RegistrationDTO dto = getRegistrationDTOFromJson(json);
        Registration savedRegistration = mapper.fromDTO(dto);
        savedRegistration.setRegistrationId(DEFAULT_UUID);
        savedRegistration.setId(1L);

        when(registrationService.save(eq(mapper.fromDTO(dto)))).thenReturn(savedRegistration);
    
        mockMvc.perform(
                post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
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

    @Test
    void postRequest_DuplicateRegistration_ShouldReturn_409() throws Exception {
    	
    	String incomingJson = "{\"eventId\": \"" + UUID.randomUUID() + "\", \"participant\": {\"email\": \"test@test.com\"}}";
         
        MockHttpServletRequestBuilder postRequest = post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(incomingJson);
        mockMvc.perform(postRequest).andReturn();
        when(registrationService.save(any())).thenThrow(ConflictingEntityException.class);
        mockMvc.perform(postRequest).andExpect(status().isConflict());
    }

    @Test
    void deleteRequest_Should_DeleteRegistration_And_Return_204_() throws Exception {
        mockMvc.perform(
                delete(PATH + "/" + DEFAULT_UUID))
                .andExpect(status().isNoContent());
        verify(registrationService, times(1)).delete(DEFAULT_UUID.toString());
    }

    private RegistrationDTO getRegistrationDTOFromJson(String incomingJson) throws JsonProcessingException {
        return new ObjectMapper().readValue(incomingJson, RegistrationDTO.class);
    }
    
    private List<RegistrationDTO> getRegistrationsFromJson(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, new TypeReference<>() {
        });
    }
}
