package com.att.training.ct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ComponentTestingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ComponentTestingApplication.class, args);
    }
}
