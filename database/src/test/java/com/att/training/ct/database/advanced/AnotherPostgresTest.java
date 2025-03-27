package com.att.training.ct.database.advanced;

import com.att.training.ct.database.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.MountableFile;

import static com.att.training.ct.database.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class AnotherPostgresTest {
    @SuppressWarnings("resource, OctalInteger")
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE)
            .withCopyToContainer(MountableFile.forClasspathResource("/db", 0755),
                    "/docker-entrypoint-initdb.d")
            .withLabel("com.att.training", "component-testing")
            .withLogConsumer(new Slf4jLogConsumer(log))
            .withReuse(false);
    @Autowired
    private UserDao userDao;

    static {
        postgres.start();
    }

    @Test
    void startDbBeforeSpringContext() {
        var count = userDao.count();
        assertThat(count).isEqualTo(2);
    }
}
