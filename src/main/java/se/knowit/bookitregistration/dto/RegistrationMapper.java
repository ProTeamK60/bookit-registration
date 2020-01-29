package se.knowit.bookitregistration.dto;

import se.knowit.bookitregistration.model.Answer;
import se.knowit.bookitregistration.model.Participant;
import se.knowit.bookitregistration.model.Registration;

import java.util.UUID;
import java.util.stream.Collectors;

public class RegistrationMapper {
    public Registration fromDTO(RegistrationDTO dto) {
        Registration registration = new Registration();
        if (notNullOrBlank(dto.getEventId())) {
            registration.setEventId(UUID.fromString(dto.getEventId()));
        }
        if (notNullOrBlank(dto.getRegistrationId())) {
            registration.setRegistrationId(UUID.fromString(dto.getRegistrationId()));
        }
        if (null != dto.getParticipant() && notNullOrBlank(dto.getParticipant().getEmail())) {
        	Participant participant = new Participant();
        	participant.setEmail(dto.getParticipant().getEmail());
        	registration.setParticipant(participant);
        }
        if (null != dto.getParticipant() && null != dto.getParticipant().getAnswers()
            && !dto.getParticipant().getAnswers().isEmpty()) {
          registration.getParticipant().setAnswers(dto.getParticipant().getAnswers().stream().map(a -> {
            return new Answer(registration.getParticipant().getId(), a.getOptionId(), a.getValue());
          }).collect(Collectors.toList()));
        }
        return registration;
    }
    
    public RegistrationDTO toDTO(Registration model) {
        
        RegistrationDTO dto = new RegistrationDTO();
        if (model.getEventId() != null) {
            dto.setEventId(model.getEventId().toString());
        }
        if (model.getRegistrationId() != null) {
            dto.setRegistrationId(model.getRegistrationId().toString());
        	ParticipantDTO participant = new ParticipantDTO();
        	participant.setEmail(model.getParticipant().getEmail());
        	dto.setParticipant(participant);

        }
        if (null != model.getParticipant() && null != model.getParticipant().getAnswers()
            && !model.getParticipant().getAnswers().isEmpty()) {
          dto.getParticipant().setAnswers(model.getParticipant().getAnswers().stream().map(a -> {return new AnswerDTO(a.getOptionId(), a.getValue());}).collect(Collectors.toList()));
          
        }
        return dto;
    }
    
    private boolean notNullOrBlank(String test) {
        return test != null && !test.isBlank();
    }
}
