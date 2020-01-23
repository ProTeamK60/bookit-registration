package se.knowit.bookitregistration.configuration.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import se.knowit.bookitregistration.dto.RegistrationDTO;
import se.knowit.bookitregistration.kafka.producer.KafkaProducerService;
import se.knowit.bookitregistration.kafka.producer.RegistrationKafkaProducerServiceImpl;
import se.knowit.bookitregistration.servicediscovery.DiscoveryService;
import se.knowit.bookitregistration.servicediscovery.DiscoveryServiceResult;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    private final DiscoveryService discoveryService;

    public KafkaProducerConfiguration(final DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Bean
    public ProducerFactory<String, RegistrationDTO> producerFactory() {
        Map<String,Object> configProps = new HashMap<>();
        DiscoveryServiceResult result = discoveryService.discoverInstances("bookit", "kafka");
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, result.getAddresses());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, RegistrationDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    @Autowired
    public KafkaProducerService<String, RegistrationDTO> registrationProducerServiceImpl(KafkaTemplate<String, RegistrationDTO> kafkaTemplate) { return new RegistrationKafkaProducerServiceImpl(kafkaTemplate); }

}
