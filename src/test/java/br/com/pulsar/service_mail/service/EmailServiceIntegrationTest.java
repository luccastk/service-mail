package br.com.pulsar.service_mail.service;

import br.com.pulsar.service_mail.dtos.PasswordChange;
import br.com.pulsar.service_mail.dtos.User;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;

import java.io.UnsupportedEncodingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@SpringBootTest
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @MockitoBean
    private JavaMailSender mailSender;

    private User user;
    private PasswordChange passwordChange;

    @BeforeEach
    void setUp() {
        user = new User(
                "Luccas",
                "teruyuki709@gmail.com"
        );

        passwordChange = new PasswordChange(
                user,
                "New Password"
        );

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);
    }

    @Test
    void shouldSendEmailWelcome() throws MessagingException, UnsupportedEncodingException {
        emailService.sendWelcomeMail(user);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void shouldSendPasswordChangeEmail() throws MessagingException, UnsupportedEncodingException {
        emailService.sendPasswordChange(passwordChange);

        verify(mailSender).send(any(MimeMessage.class));
    }
}