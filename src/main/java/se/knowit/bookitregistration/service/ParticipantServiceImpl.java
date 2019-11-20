package se.knowit.bookitregistration.service;

import java.util.Set;
import java.util.stream.Collectors;

import se.knowit.bookitregistration.dto.ParticipantDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;

public class ParticipantServiceImpl implements ParticipantService {
	private final RegistrationService registrationService;
	private final RegistrationMapper registrationMapper;
	
	public ParticipantServiceImpl(
			final RegistrationService registrationService,
			final RegistrationMapper registrationMapper) 
	{
		this.registrationService = registrationService;
		this.registrationMapper = registrationMapper;
	}
	
	@Override
	public Set<ParticipantDTO> getParticipantsByEventId(String eventId) {
		return registrationService.findRegistrationsByEventId(eventId).stream()
				.map(registration -> registrationMapper.toDTO(registration))
				.map(registrationDto -> registrationDto.getParticipant())
				.collect(Collectors.toSet());
	}

}
