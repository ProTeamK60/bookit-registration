package se.knowit.bookitregistration.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import se.knowit.bookitevent.dto.EventDTO;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;
    private String groupId = "1";

    @Bean
    public ConsumerFactory<String, EventDTO> consumerFactory() {
        JsonDeserializer<EventDTO> eventDeserializer = new JsonDeserializer<>(EventDTO.class);
        eventDeserializer.setRemoveTypeHeaders(false);
        eventDeserializer.setUseTypeMapperForKey(true);
        eventDeserializer.addTrustedPackages("se.knowit");

        Map<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), eventDeserializer);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<String,EventDTO> kafkaListenerContainerFactory(final ConsumerFactory<String, EventDTO> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String,EventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
