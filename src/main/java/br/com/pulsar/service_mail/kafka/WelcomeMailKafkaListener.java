package br.com.pulsar.service_mail.kafka;

import br.com.pulsar.service_mail.dtos.User;
import br.com.pulsar.service_mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class WelcomeMailKafkaListener {

    private final EmailService emailService;

    @RetryableTopic(
            backoff = @Backoff(delay = 3000L),
            autoCreateTopics = "true",
            include = {MessagingException.class, UnsupportedEncodingException.class},
            listenerContainerFactory = "welcomeListenerFactory",
            kafkaTemplate = "welcomeKafkaTemplate"
    )
    @KafkaListener(
            topics = "mail.welcome",
            groupId = "mail-send-consumer",
            containerFactory = "welcomeListenerFactory"
    )
    public void send(User user) throws MessagingException, UnsupportedEncodingException {
        emailService.sendWelcomeMail(user);
    }
}
