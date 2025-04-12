package br.com.pulsar.service_mail.service;

import br.com.pulsar.service_mail.dtos.PasswordChange;
import br.com.pulsar.service_mail.dtos.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final Environment environment;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendWelcomeMail(User request) throws MessagingException, UnsupportedEncodingException {
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", request.email());

        final String htmlContent = this.templateEngine.process("welcome-email", ctx);

       mailSender.send(
                prepareEmail(request, htmlContent)
        );
    }

    public void sendPasswordChange(PasswordChange request) throws MessagingException, UnsupportedEncodingException {
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("name", request.user().name());
        ctx.setVariable("temporaryPassword", request.password());

        final String htmlContent = this.templateEngine.process("password-change", ctx);

        mailSender.send(
                prepareEmail(request.user(), htmlContent)
        );
    }

    private MimeMessage prepareEmail(User user, String htmlContent) throws MessagingException, UnsupportedEncodingException {
        String mailFrom
                = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String mailFromName
                = environment.getProperty("mail.from.name", "Identity");

        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper helper
                = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(user.email());
        helper.setSubject("Welcome Pulsar");
        helper.setFrom(new InternetAddress(mailFrom, mailFromName));

        helper.setText(htmlContent, true);

        return mimeMessage;
    }
}
