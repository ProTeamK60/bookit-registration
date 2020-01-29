package se.knowit.bookitregistration.dto;

import java.util.List;

import lombok.Data;

@Data
public class ParticipantDTO {
	
	private String email;
	private List<AnswerDTO> answers;
    
}
