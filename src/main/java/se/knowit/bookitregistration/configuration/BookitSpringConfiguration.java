package se.knowit.bookitregistration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitregistration.service.map.RegistrationServiceMapImpl;
import se.knowit.bookitregistration.service.RegistrationService;

@Configuration
public class BookitSpringConfiguration {

    @Profile("map")
    @Bean
    public RegistrationService mapBasedRegistrationServiceImpl() {return new RegistrationServiceMapImpl(); }
}
