package com.att.training.ct.spring;

import com.att.training.ct.user.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.att.training.ct.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestContainersSingletonTest {
    @SuppressWarnings("resource")
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE)
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("pwd")
            .withInitScript("db/init.sql");

    static {
        postgres.start();
    }

    @Autowired
    private UserDao userDao;

    @Test
    void startDbBeforeSpringContext() {
        var count = userDao.count();
        assertThat(count).isEqualTo(2);
    }

}
