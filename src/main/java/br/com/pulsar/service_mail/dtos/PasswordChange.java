package br.com.pulsar.service_mail.dtos;

public record PasswordChange(
        User user,
        String password
) {
}
