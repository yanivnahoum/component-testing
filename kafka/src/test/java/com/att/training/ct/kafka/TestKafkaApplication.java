package com.att.training.ct.kafka;

import org.springframework.boot.SpringApplication;

public class TestKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.from(KafkaApplication::main)
                .with(ContainerConfiguration.class)
                .run(args);
    }
}
