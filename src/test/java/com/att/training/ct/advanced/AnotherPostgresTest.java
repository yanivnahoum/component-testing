package com.att.training.ct.advanced;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.MountableFile;

import static com.att.training.ct.PostgresTestImages.DEFAULT_IMAGE;

@Slf4j
class AnotherPostgresTest {
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE)
            .withDatabaseName("some_db")
            .withUsername("some_user")
            .withPassword("some_password")
            .withLabel("com.att.training", "component-testing")
            .withLogConsumer(new Slf4jLogConsumer(log))
            .withCopyFileToContainer(MountableFile.forClasspathResource("db"),
                    "/docker-entrypoint-initdb.d")
            .withReuse(false);

    static {
        postgres.start();
    }

    @Test
    void test1() {
        logDbInfo();
    }

    private void logDbInfo() {
        log.info("Postgres is up and running! url={}, user={}, password={}",
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }
}
