package se.knowit.bookitregistration.dto;

import se.knowit.bookitregistration.model.Registration;

import java.util.UUID;

public class RegistrationMapper {
    public Registration fromDTO(RegistrationDTO dto) {
        Registration registration = new Registration();
        if (notNullOrBlank(dto.getEventId())) {
            registration.setEventId(UUID.fromString(dto.getEventId()));
        }
        registration.setEmail(dto.getEmail());
        return registration;
    }

    public RegistrationDTO toDTO(Registration model) {
        RegistrationDTO dto = new RegistrationDTO();
        if (model.getEventId() != null) {
            dto.setEventId(model.getEventId().toString());
        }
        dto.setEmail(model.getEmail());
        return dto;
    }

    private boolean notNullOrBlank(String test) {
        return test != null && !test.isBlank();
    }
}
