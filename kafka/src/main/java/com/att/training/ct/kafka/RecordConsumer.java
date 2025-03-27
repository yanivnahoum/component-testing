package com.att.training.ct.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordConsumer {
    private final RecordProcessor recordProcessor;

    @KafkaListener(topics = Kafka.MAIN_TOPIC)
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        recordProcessor.process(consumerRecord.key(), consumerRecord.value());
    }
}
