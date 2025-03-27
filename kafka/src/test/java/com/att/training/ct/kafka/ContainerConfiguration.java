package com.att.training.ct.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.TopicBuilder;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@TestConfiguration(proxyBeanMethods = false)
class ContainerConfiguration {
    @ServiceConnection
    @Bean
    ConfluentKafkaContainer kafkaContainer() {
        return new ConfluentKafkaContainer("confluentinc/cp-kafka:7.8.2");
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(Kafka.MAIN_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}

@Retention(RUNTIME)
@Target(TYPE)
@Import(ContainerConfiguration.class)
@interface WithKafkaContainer {}
