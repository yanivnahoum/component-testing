package com.att.training.ct.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.KafkaHeaders.GROUP_ID;

@Component
@Slf4j
public class RecordConsumer {
    @KafkaListener(topics = Kafka.MAIN_TOPIC)
    public void consumeAsConsumerRecord(ConsumerRecord<?, ?> consumerRecord) {
        log.info("Consumed consumerRecord: {}, headers: {}", consumerRecord, consumerRecord.headers());
    }

    @KafkaListener(topics = Kafka.MAIN_TOPIC, groupId = "spring-examples-kafka-string")
    public void consumeAsString(String payload, @Header(GROUP_ID) String groupId) {
        log.info("Consumed payload: {}, groupId: {} ", payload, groupId);
    }
}
