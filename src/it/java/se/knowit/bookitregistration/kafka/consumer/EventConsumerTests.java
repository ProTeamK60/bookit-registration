package se.knowit.bookitregistration.kafka.consumer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.knowit.bookitregistration.dto.event.EventDTO;
import se.knowit.bookitregistration.dto.event.EventMapper;
import se.knowit.bookitregistration.model.event.Event;
import se.knowit.bookitregistration.repository.EventRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EmbeddedKafka(partitions = 1,
        topics = {EventConsumerTests.TOPIC},
        brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EventConsumerTests {

    static final String TOPIC = "events";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    KafkaListenerEndpointRegistry registry;

    private KafkaTemplate<String, EventDTO> kafkaTemplate;

    private EventMapper mapper = new EventMapper();

    @BeforeEach
    void setup() {
        kafkaTemplate = createKafkaTemplate();
    }

    @Test
    void test_consumeEvent() throws InterruptedException {
        Event event = createEvent();
        ConcurrentMessageListenerContainer<?, ?> container = (ConcurrentMessageListenerContainer<?, ?>) registry.getListenerContainer("event-listener");
        container.stop();
        AcknowledgingConsumerAwareMessageListener<String, EventDTO> messageListener = (AcknowledgingConsumerAwareMessageListener<String, EventDTO>) container.getContainerProperties().getMessageListener();
        CountDownLatch latch = new CountDownLatch(1);
        container.getContainerProperties().setMessageListener((AcknowledgingConsumerAwareMessageListener<String, EventDTO>) (record, acknowledgment, consumer) -> {
            messageListener.onMessage(record, acknowledgment, consumer);
            latch.countDown();
        });
        container.start();

        EventDTO dto = mapper.toDTO(event);
        kafkaTemplate.send(TOPIC, dto.getEventId(), dto);
        Assertions.assertTrue(latch.await(15, TimeUnit.SECONDS));
        Event savedEvent = eventRepository.findByEventId(event.getEventId()).orElseThrow();
        Assertions.assertEquals(event, savedEvent);
    }

    private Event createEvent() {
        Event event = new Event();
        event.setId(1L);
        event.setEventId(UUID.randomUUID());
        event.setName("test event");
        event.setDescription("description");
        event.setOrganizer("org");
        event.setLocation("somewhere");
        event.setDeadlineRVSP(Instant.ofEpochMilli(10000L));
        event.setEventStart(Instant.ofEpochMilli(20000L));
        event.setEventEnd(Instant.ofEpochMilli(30000L));
        return event;
    }

    private ProducerFactory<String, EventDTO> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<>());
    }

    private KafkaTemplate<String, EventDTO> createKafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }

}
