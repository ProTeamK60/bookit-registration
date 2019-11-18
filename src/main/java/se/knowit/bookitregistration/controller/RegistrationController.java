package se.knowit.bookitregistration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitregistration.dto.RegistrationDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;

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

    public RegistrationController(RegistrationService service) {

        this.service = service;
        this.mapper = new RegistrationMapper();
    }

    @GetMapping({"", "/"})
    public Set<RegistrationDTO> findAllRegistrations() {
        Set<Registration> allRegistrations = service.findAll();
        if (allRegistrations.isEmpty()) {
            throw notFound();
        }
        RegistrationMapper mapper = new RegistrationMapper();
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
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }

    private RegistrationSaveResult trySave(@RequestBody RegistrationDTO incomingRegistration) {
        try {
            Registration saved = service.save(mapper.fromDTO(incomingRegistration));
            return new RegistrationSaveResult(saved.getRegistrationId().toString());
        } catch(Exception e)
        {
            return new RegistrationSaveResult(e);
        }
    }

    private ResponseEntity<String> buildResponseFromRegistrationSaveResult(RegistrationSaveResult result)
    {
        if(result.outcome == Outcome.CREATED) {
            return ResponseEntity.created(getUri(result.registrationId)).build();
        }
        else {
            StringWriter stringWriter = new StringWriter();
            result.throwable.printStackTrace(new PrintWriter(stringWriter));
            return ResponseEntity.badRequest().body(stringWriter.toString());
        }
    }

    private URI getUri(String uri) { return URI.create(BASE_URI.getPath() + uri); }

    private static class RegistrationSaveResult {
        private String registrationId;
        private Throwable throwable;
        private Outcome outcome;

        public RegistrationSaveResult() {}
        RegistrationSaveResult(String registrationId)
        {
            this.registrationId = registrationId;
            this.outcome = Outcome.CREATED;
        }
        RegistrationSaveResult(Throwable throwable)
        {
            this.throwable = throwable;
            this.outcome = Outcome.FAILED;
        }

    }

    private enum Outcome
    {
        CREATED,
        FAILED
    }
}
