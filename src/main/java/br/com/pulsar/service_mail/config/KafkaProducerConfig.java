package br.com.pulsar.service_mail.config;

import br.com.pulsar.service_mail.dtos.User;
import br.com.pulsar.service_mail.kafka.PasswordChangeKafkaListener;
import com.pulsar.common.common_kafka.dispatcher.KafkaEventDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = "passwordChangeKafkaTemplate")
    public KafkaTemplate<String, PasswordChangeKafkaListener> kafkaTemplatePasswordChange() {
        return KafkaEventDispatcher.createTemplate(bootstrapServers);
    }

    @Bean(name = "welcomeKafkaTemplate")
    public KafkaTemplate<String, User> kafkaTemplateWelcome() {
        return KafkaEventDispatcher.createTemplate(bootstrapServers);
    }
}
