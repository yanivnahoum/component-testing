package com.att.training.ct.kafka;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.kafka.ConfluentKafkaContainer;

@TestConfiguration(proxyBeanMethods = false)
class ContainerConfiguration {
    @ServiceConnection
    @Bean
    ConfluentKafkaContainer kafkaContainer() {
        return new ConfluentKafkaContainer("confluentinc/cp-kafka:7.9.2");
    }
}

