package br.com.pulsar.service_mail.kafka;

import br.com.pulsar.service_mail.dtos.PasswordChange;
import br.com.pulsar.service_mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enable", havingValue = "true", matchIfMissing = false)
public class PasswordChangeKafkaListener {

    private final EmailService emailService;

    @RetryableTopic(
            backoff = @Backoff(delay = 3000L),
            autoCreateTopics = "true",
            include = {MessagingException.class, UnsupportedEncodingException.class},
            listenerContainerFactory = "passwordChangeListenerFactory",
            kafkaTemplate = "passwordChangeKafkaTemplate"
    )
    @KafkaListener(
            topics = "mail.password-change",
            groupId = "mail-send-consumer",
            containerFactory = "passwordChangeListenerFactory"
    )
    public void send(PasswordChange event) throws MessagingException, UnsupportedEncodingException {
        emailService.sendPasswordChange(event);
    }
}
