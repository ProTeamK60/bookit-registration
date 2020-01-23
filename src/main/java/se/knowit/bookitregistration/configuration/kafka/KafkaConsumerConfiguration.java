package se.knowit.bookitregistration.configuration.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import se.knowit.bookitregistration.dto.event.EventDTO;
import se.knowit.bookitregistration.kafka.consumer.EventConsumer;
import se.knowit.bookitregistration.repository.EventRepository;
import se.knowit.bookitregistration.servicediscovery.DiscoveryService;
import se.knowit.bookitregistration.servicediscovery.DiscoveryServiceResult;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

    private final DiscoveryService discoveryService;

    public KafkaConsumerConfiguration(final DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Bean
    public ConsumerFactory<String, EventDTO> consumerFactory() {
        DiscoveryServiceResult result = discoveryService.discoverInstances("bookit", "kafka");
        Map<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, result.getAddresses());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "event-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(EventDTO.class, false));
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<String,EventDTO> kafkaListenerContainerFactory(final ConsumerFactory<String, EventDTO> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String,EventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    @Autowired
    public EventConsumer eventConsumer(EventRepository eventRepository) { return new EventConsumer(eventRepository); }

}
