package se.knowit.bookitregistration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import se.knowit.bookitnotification.dto.NotificationDTO;

public class KafkaServiceImpl implements KafkaService {

    @Autowired
    public KafkaTemplate<String,NotificationDTO> kafkaTemplate;

    @Override
    public void sendMessage(String topic, NotificationDTO notification) {
        ListenableFuture<SendResult<String,NotificationDTO>> future = kafkaTemplate.send(topic, notification);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Unable to send message=["
                        + notification + "] due to : " + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, NotificationDTO> result) {
                System.out.println("Sent message=[" + notification +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }
}
