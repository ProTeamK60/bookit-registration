package se.knowit.bookitregistration.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import se.knowit.bookitregistration.dto.RegistrationDTO;

public class RegistrationKafkaProducerServiceImpl implements KafkaProducerService<String, RegistrationDTO> {

    private final KafkaTemplate<String, RegistrationDTO> kafkaTemplate;

    public RegistrationKafkaProducerServiceImpl(KafkaTemplate<String,RegistrationDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(final String topic, final String key, final RegistrationDTO message) {
        ListenableFuture<SendResult<String, RegistrationDTO>> listenableFuture = kafkaTemplate.send(topic, key, message);
        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                //TODO: log.
            }

            @Override
            public void onSuccess(SendResult<String, RegistrationDTO> stringRegistrationDTOSendResult) {
                //TODO: log.
            }
        });
    }
}
