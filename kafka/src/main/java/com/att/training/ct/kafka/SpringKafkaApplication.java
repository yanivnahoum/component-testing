package com.att.training.ct.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaApplication.class, args);
    }
}
