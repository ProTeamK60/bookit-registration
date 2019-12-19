package se.knowit.bookitregistration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitregistration.dto.RegistrationMapper;
import se.knowit.bookitregistration.repository.RegistrationRepository;
import se.knowit.bookitregistration.repository.map.RegistrationRepositoryMapImpl;
import se.knowit.bookitregistration.service.*;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    @Autowired
    public RegistrationService registrationServiceImpl(final RegistrationRepository registrationRepository) {
        return new RegistrationServiceImpl(registrationRepository);
    }

    @Profile("map")
    @Bean
    public RegistrationRepository mapBasedRegistrationRepositoryImpl() {
        return new RegistrationRepositoryMapImpl();
    }

    @Bean
    @Autowired
    public ParticipantService participantServiceImpl(final RegistrationService registrationService) {
        return new ParticipantServiceImpl(registrationService, new RegistrationMapper());
    }

    @Bean
    public KafkaService kafkaServiceImpl() {
        return new KafkaServiceImpl();
    }


}
