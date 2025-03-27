package com.att.training.ct.kafka;

import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class KafkaHealthIndicator extends AbstractHealthIndicator {
    private final AdminClient adminClient;

    public KafkaHealthIndicator(KafkaAdmin kafkaAdmin) {
        adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }

    @Override
    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    protected void doHealthCheck(Health.Builder builder) {
        var topicNames = adminClient.listTopics().names().get();
        if (topicNames.isEmpty()) {
            log.warn("#doHealthCheck - Kafka topics missing!");
            builder.down()
                    .withDetail("kafka", "unavailable")
                    .withDetail("topics", "<empty>");
        } else {
            builder.up()
                    .withDetail("kafka", "available")
                    .withDetail("topics", topicNames);
        }
    }

    @PreDestroy
    public void shutdown() {
        adminClient.close();
    }
}
