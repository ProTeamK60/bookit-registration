package se.knowit.bookitregistration.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import se.knowit.bookitregistration.dto.ParticipantDTO;
import se.knowit.bookitregistration.service.ParticipantService;

@RestController
@RequestMapping(ParticipantController.BASE_PATH)
@CrossOrigin()
public class ParticipantController {

	static final String BASE_PATH = "/api/v1/participants";

    private final ParticipantService service;

    public ParticipantController(ParticipantService service) {
        this.service = service;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Set<ParticipantDTO>> getParticipantsByEventId(@PathVariable String eventId) {
    	final Set<ParticipantDTO> participants = service.getParticipantsByEventId(eventId);
    	if(participants.isEmpty()) {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participants for eventID " + eventId + " not found!");
    	}
    	
    	return ResponseEntity.ok(participants);
    }
}
