package se.knowit.bookitregistration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitregistration.dto.ParticipantDTO;
import se.knowit.bookitregistration.service.ParticipantService;

import java.util.Set;

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
        if (participants.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participants for eventID " + eventId + " not found!");
        }
        
        return ResponseEntity.ok(participants);
    }
}
