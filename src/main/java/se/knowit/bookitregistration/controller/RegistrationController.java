package se.knowit.bookitregistration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.knowit.bookitregistration.dto.RegistrationDTO;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.model.Registration;
import se.knowit.bookitregistration.service.RegistrationService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(RegistrationController.BASE_PATH)
@CrossOrigin()
public class RegistrationController {
    static final String BASE_PATH = "/api/v1/registrations";
    private static final URI BASE_URI = URI.create(BASE_PATH + "/");
    private RegistrationMapper mapper = new RegistrationMapper();
    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping(value = {"", "/"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerToEvent(@RequestBody RegistrationDTO dto) {
        Registration registration = service.save(mapper.fromDTO(dto));
        return ResponseEntity.created(getUri(registration.getRegistrationId().toString())).build();
    }

    private URI getUri(String uri) { return URI.create(BASE_URI.getPath() + uri); }
}
