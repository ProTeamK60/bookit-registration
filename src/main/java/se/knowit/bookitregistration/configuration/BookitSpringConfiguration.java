package se.knowit.bookitregistration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.repository.EventRepository;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.repository.map.EventRepositoryMapImpl;
import se.knowit.bookitregistration.repository.map.RegistrationRepositoryMapImpl;
import se.knowit.bookitregistration.service.ParticipantService;
import se.knowit.bookitregistration.service.ParticipantServiceImpl;
import se.knowit.bookitregistration.service.RegistrationService;
import se.knowit.bookitregistration.service.RegistrationServiceImpl;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    @Autowired
    public RegistrationService registrationServiceImpl(final RegistrationRepository registrationRepository, final EventRepository eventRepository) {
        return new RegistrationServiceImpl(registrationRepository, eventRepository);
    }

    @Bean
    @Autowired
    public ParticipantService participantServiceImpl(final RegistrationService registrationService) {
        return new ParticipantServiceImpl(registrationService, new RegistrationMapper());
    }

    @Bean
    public RegistrationRepository mapBasedRegistrationRepositoryImpl() {
        return new RegistrationRepositoryMapImpl();
    }

    @Bean
    public EventRepository mapBasedEventRepositoryImpl() { return new EventRepositoryMapImpl(); }

}
