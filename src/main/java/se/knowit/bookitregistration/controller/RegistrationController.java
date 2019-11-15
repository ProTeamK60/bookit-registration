package se.knowit.bookitregistration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.knowit.bookitregistration.dto.RegistrationDTO;

@RestController
@RequestMapping(RegistrationController.BASE_PATH)
@CrossOrigin()
public class RegistrationController {
    static final String BASE_PATH = "/api/v1/registrations";

    @PostMapping(value = {"", "/"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerToEvent(@RequestBody RegistrationDTO dto) {

    }
}
