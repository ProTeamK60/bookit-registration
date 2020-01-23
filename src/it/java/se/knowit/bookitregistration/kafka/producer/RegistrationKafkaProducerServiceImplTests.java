package se.knowit.bookitregistration.kafka.producer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.knowit.bookitregistration.dto.ParticipantDTO;
import se.knowit.bookitregistration.dto.RegistrationDTO;

import java.util.Map;
import java.util.UUID;

@EmbeddedKafka(partitions = 1,
        topics = {RegistrationKafkaProducerServiceImplTests.TOPIC},
        brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class RegistrationKafkaProducerServiceImplTests {

    static final String TOPIC = "registrations";

    @Autowired
    private KafkaProducerService<String, RegistrationDTO> producerService;
    
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker kafkaBroker;

    @Test
    void test_sendRegistrationDto_to_topic() {
        RegistrationDTO dto = createDto();
        producerService.sendMessage(TOPIC, dto.getRegistrationId(), dto);
        final Consumer<String, RegistrationDTO> consumer = buildConsumer();
        kafkaBroker.consumeFromAnEmbeddedTopic(consumer, TOPIC);
        ConsumerRecord<String, RegistrationDTO> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        Assertions.assertEquals(dto, singleRecord.value());
    }

    private Consumer<String, RegistrationDTO> buildConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testSendEvent", "true", kafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        ConsumerFactory<String, RegistrationDTO> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new JsonDeserializer<>(RegistrationDTO.class, false));
        return consumerFactory.createConsumer();
    }

    private RegistrationDTO createDto() {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setRegistrationId(UUID.randomUUID().toString());
        dto.setEventId(UUID.randomUUID().toString());
        dto.setParticipant(new ParticipantDTO());
        dto.getParticipant().setEmail("test@test.com");
        return dto;
    }
}
