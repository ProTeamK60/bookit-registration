package se.knowit.bookitregistration.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import se.knowit.bookitregistration.dto.event.EventDTO;
import se.knowit.bookitregistration.dto.event.EventMapper;
import se.knowit.bookitregistration.repository.EventRepository;

public class EventConsumer {

    private final EventRepository eventRepository;
    private final EventMapper mapper;

    public EventConsumer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.mapper = new EventMapper();
    }

    @KafkaListener(id = "event-listener",
            topics = {"events"},
            groupId = "event-consumer",
            containerFactory = "kafkaListenerContainerFactory",
            topicPartitions = @TopicPartition(
                    topic = "events",
                    partitionOffsets = @PartitionOffset(
                            partition = "0",
                            initialOffset = "0"
                    )
            )
    )
    public void consumeMessage(EventDTO event) {
        eventRepository.save(mapper.fromDTO(event));
    }

}
