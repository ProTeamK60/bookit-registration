package se.knowit.bookitregistration.configuration;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitregistration.servicediscovery.AwsDiscoveryServiceImpl;
import se.knowit.bookitregistration.servicediscovery.DiscoveryService;

@Profile("prod")
@Configuration
public class ProdConfiguration {

    @Value("aws.discovery.region")
    private String awsRegion;

    @Bean
    @Autowired
    public DiscoveryService AwsDiscoveryServiceImpl(AWSServiceDiscovery serviceDiscovery) { return new AwsDiscoveryServiceImpl(serviceDiscovery); }

    @Bean
    public AWSServiceDiscovery awsServiceDiscoveryClient() {
        return AWSServiceDiscoveryClientBuilder
                .standard()
                .withRegion(awsRegion)
                .build();
    }

}
