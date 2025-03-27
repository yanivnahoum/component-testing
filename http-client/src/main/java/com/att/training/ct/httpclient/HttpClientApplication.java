package com.att.training.ct.httpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HttpClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(HttpClientApplication.class, args);
    }
}
