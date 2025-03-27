
package com.att.training.ct.database.spring;

import com.att.training.ct.database.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.att.training.ct.database.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.flyway.enabled=true")
class FlywayTest {
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE);
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
