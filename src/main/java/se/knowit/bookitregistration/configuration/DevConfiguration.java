package se.knowit.bookitregistration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import se.knowit.bookitregistration.servicediscovery.DiscoveryService;
import se.knowit.bookitregistration.servicediscovery.LocalDiscoveryServiceImpl;

@Profile("dev")
@Configuration
public class DevConfiguration {

    @Bean
    @Autowired
    public DiscoveryService localDiscoveryServiceImpl(Environment environment) { return new LocalDiscoveryServiceImpl(environment); }

}
