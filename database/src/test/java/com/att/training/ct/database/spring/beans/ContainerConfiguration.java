package com.att.training.ct.database.spring.beans;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.att.training.ct.database.PostgresTestImages.DEFAULT_IMAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@TestConfiguration(proxyBeanMethods = false)
public class ContainerConfiguration {
    @SuppressWarnings("resource")
    @ServiceConnection
    @Bean
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DEFAULT_IMAGE)
                .withInitScript("db/init.sql");
    }
}

@Target(TYPE)
@Retention(RUNTIME)
@Import(ContainerConfiguration.class)
@interface WithPostgres {}
