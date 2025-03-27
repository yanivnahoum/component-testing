package com.att.training.ct.database.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.att.training.ct.database.PostgresTestImages.DEFAULT_IMAGE;

/**
 * The {@code @TestContainers} meta-annotation is basically a junit jupiter extension that looks for
 * fields that are annotated with {@code @Container} and manages their lifecycle.
 * If the field is static, the container will be started before all tests and stopped after all tests.
 * Otherwise, if it's an instance field, then the container will be started before each test in the class
 * and stopped after each test.
 */
@Slf4j
@Testcontainers
class ContainerLifecycleTest {
    @Container
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE);

    @Test
    void test1() {
        logDbInfo();
    }

    @Test
    void test2() {
        logDbInfo();
    }

    private void logDbInfo() {
        log.info("Postgres is up and running! url={}, user={}, password={}",
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }
}
