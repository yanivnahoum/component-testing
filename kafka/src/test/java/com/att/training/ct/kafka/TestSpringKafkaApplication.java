package com.att.training.ct.kafka;

import org.springframework.boot.SpringApplication;

public class TestSpringKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.from(SpringKafkaApplication::main)
                .with(ContainerConfiguration.class)
                .run(args);
    }
}
