package se.knowit.bookitregistration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.service.ParticipantService;
import se.knowit.bookitregistration.service.ParticipantServiceImpl;

import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.map.RegistrationServiceMapImpl;

@Configuration
public class BookitSpringConfiguration {

    @Profile("map")
    @Bean
    public RegistrationService mapBasedRegistrationServiceImpl() {return new RegistrationServiceMapImpl(); }
    
    @Bean
    @Autowired
    public ParticipantService participantServiceImpl(final RegistrationService registrationService) {
    	return new ParticipantServiceImpl(registrationService, new RegistrationMapper()); 
    }

}
