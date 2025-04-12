package br.com.pulsar.service_mail.config;

import br.com.pulsar.service_mail.dtos.PasswordChange;
import br.com.pulsar.service_mail.dtos.User;
import com.pulsar.common.common_kafka.consumer.KafkaEventConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @Bean(name = "welcomeListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, User> kafkaListenerContainerFactory1() {
        return KafkaEventConsumer.createFactory(User.class, bootstrapServers);
    }

    @Bean(name = "passwordChangeListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, PasswordChange> kafkaListenerContainerFactory2() {
        return KafkaEventConsumer.createFactory(PasswordChange.class, bootstrapServers);
    }
}
