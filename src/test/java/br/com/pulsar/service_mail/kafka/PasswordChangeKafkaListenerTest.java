package br.com.pulsar.service_mail.kafka;

import br.com.pulsar.service_mail.dtos.PasswordChange;
import br.com.pulsar.service_mail.dtos.User;
import br.com.pulsar.service_mail.service.EmailService;
import jakarta.mail.MessagingException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Map;

import static org.apache.kafka.server.config.ConfigType.TOPIC;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@EnableKafka
@EmbeddedKafka(
        partitions = 1,
        topics = "mail.password-change",
        ports = { 0 },
        brokerProperties = {"listeners=PLAINTEXT://localhost:0"})
@SpringBootTest(properties = {
        "kafka.enable=true",
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
})
class PasswordChangeKafkaListenerTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private PasswordChangeKafkaListener passwordChangeKafkaListener;

    @MockitoBean
    private EmailService emailService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "Test",
                "test@test.com"
        );
    }

    private <V> Consumer<String, V> createConsumer(Class<V> classType) {

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(TOPIC, "true", this.embeddedKafkaBroker);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        DefaultKafkaConsumerFactory<String, V> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProps, new StringDeserializer(), new JsonDeserializer<>()
        );

        return consumerFactory.createConsumer();
    }

    @Test
    void shouldConsumePasswordChangeEvent() throws MessagingException, UnsupportedEncodingException {
        Consumer<String, PasswordChange> consumer = createConsumer(PasswordChange.class);
        this.embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, "mail.password-change");
        PasswordChange passwordChange = new PasswordChange(
                user,
                "newpassword"
        );

        passwordChangeKafkaListener.send(passwordChange);

        ConsumerRecords<String, PasswordChange> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        verify(emailService)
                                .sendPasswordChange(eq(passwordChange))
                );
    }
}