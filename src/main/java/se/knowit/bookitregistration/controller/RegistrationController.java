package se.knowit.bookitregistration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitregistration.dto.RegistrationDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.kafka.producer.KafkaProducerService;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.exception.ConflictingEntityException;
import se.knowit.bookitregistration.service.exception.MaxNumberOfRegistrationExceededException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping(RegistrationController.BASE_PATH)
@CrossOrigin()
public class RegistrationController {
    static final String BASE_PATH = "/api/v1/registrations";
    private static final URI BASE_URI = URI.create(BASE_PATH + "/");

    private final RegistrationMapper mapper;
    private final RegistrationService service;
    private final KafkaProducerService<String, RegistrationDTO> registrationProducer;

    public RegistrationController(RegistrationService service, KafkaProducerService<String,RegistrationDTO> registrationProducer) {
        this.mapper = new RegistrationMapper();
        this.service = service;
        this.registrationProducer = registrationProducer;
    }

    @GetMapping({"", "/"})
    public Set<RegistrationDTO> findAllRegistrations() {
        Set<Registration> allRegistrations = service.findAll();
        if (allRegistrations.isEmpty()) {
            throw notFound();
        }
        return allRegistrations.stream()
                .map(mapper::toDTO)
                .collect(toSet());
    }

    @PostMapping(value = {"", "/"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerToEvent(@RequestBody RegistrationDTO dto) {
        RegistrationSaveResult result = trySave(dto);
        return buildResponseFromRegistrationSaveResult(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByRegistrationId(@PathVariable String id) {
        service.deleteByRegistrationId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{email}/{eventId}")
    public ResponseEntity<String> deleteByEmailAndEventId(@PathVariable String email, @PathVariable String eventId) {
        service.deleteByEventIdAndEmail(eventId, email);
        return ResponseEntity.noContent().build();
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }

    private RegistrationSaveResult trySave(RegistrationDTO incomingRegistration) {
        try {
            Registration saved = service.save(mapper.fromDTO(incomingRegistration));
            RegistrationDTO savedDto = mapper.toDTO(saved);
            registrationProducer.sendMessage("registrations", savedDto.getRegistrationId(), savedDto);
            return new RegistrationSaveResult(saved.getRegistrationId().toString());
        } catch (ConflictingEntityException conflictingEntityException) {
            return new RegistrationSaveResult(conflictingEntityException, Outcome.CONFLICT);
        } catch (IllegalArgumentException | NullPointerException e) {
            return new RegistrationSaveResult(e, Outcome.FAILED);
        } catch (MaxNumberOfRegistrationExceededException mn) {
          return new RegistrationSaveResult(mn, Outcome.MAX_LIMIT_REACHED);
        }
    }

    private ResponseEntity<String> buildResponseFromRegistrationSaveResult(RegistrationSaveResult result) {
        if (result.outcome == Outcome.CREATED) {
            return ResponseEntity.created(getUri(result.registrationId)).build();
        } else if (result.outcome == Outcome.CONFLICT) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result.throwable.getMessage());
        } else if (result.outcome == Outcome.MAX_LIMIT_REACHED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result.throwable.getMessage());
        } else {
            StringWriter stringWriter = new StringWriter();
            result.throwable.printStackTrace(new PrintWriter(stringWriter));
            return ResponseEntity.badRequest().body(stringWriter.toString());
        }
    }

    private URI getUri(String uri) {
        return URI.create(BASE_URI.getPath() + uri);
    }

    private static class RegistrationSaveResult {
        private String registrationId;
        private Throwable throwable;
        private Outcome outcome;

        RegistrationSaveResult(String registrationId) {
            this.registrationId = registrationId;
            this.outcome = Outcome.CREATED;
        }

        RegistrationSaveResult(Throwable throwable, Outcome outcome) {
            this.throwable = throwable;
            this.outcome = outcome;
        }

    }

    private enum Outcome {
        CREATED,
        CONFLICT,
        FAILED,
        MAX_LIMIT_REACHED
    }
}
