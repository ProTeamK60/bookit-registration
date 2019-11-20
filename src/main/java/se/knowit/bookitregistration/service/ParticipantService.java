package se.knowit.bookitregistration.service;

import java.util.Set;

import se.knowit.bookitregistration.dto.ParticipantDTO;

public interface ParticipantService {
	Set<ParticipantDTO> getParticipantsByEventId(String eventId);
}
