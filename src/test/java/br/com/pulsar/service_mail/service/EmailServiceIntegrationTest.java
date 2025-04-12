package br.com.pulsar.service_mail.service;

import br.com.pulsar.service_mail.dtos.User;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;


@SpringBootTest
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void shouldSendEmailWelcome() throws MessagingException, UnsupportedEncodingException {
        User user = new User(
                "Luccas",
                "teruyuki709@gmail.com"
        );

        emailService.sendWelcomeMail(user);
    }
}