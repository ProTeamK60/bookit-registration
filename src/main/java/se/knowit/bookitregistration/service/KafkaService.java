package se.knowit.bookitregistration.service;

import se.knowit.bookitnotification.dto.NotificationDTO;

public interface KafkaService {
    void sendMessage(String topic, NotificationDTO notification);
}
