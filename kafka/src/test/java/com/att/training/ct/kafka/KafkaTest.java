package com.att.training.ct.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.kafka.consumer.auto-offset-reset=earliest")
@WithKafkaContainer
class KafkaTest {
    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;
    @MockitoBean
    private RecordProcessor recordProcessor;

    @Test
    void givenRecord_whenSendToTopic_thenRecordIsProcessed() {
        int key = 1234;
        var value = "some-value";
        kafkaTemplate.send(Kafka.MAIN_TOPIC, key, value).join();

        await().atMost(Duration.ofSeconds(3))
                .untilAsserted(() -> verify(recordProcessor).process(key, value));
    }
}
