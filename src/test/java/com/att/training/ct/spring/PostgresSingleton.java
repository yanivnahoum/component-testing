package com.att.training.ct.spring;

import com.att.training.ct.user.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.att.training.ct.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Slf4j
abstract class PostgresSingleton {
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

    @BeforeEach
    void logDbInfo() {
        log.info("Postgres is up and running! url={}, user={}, password={}",
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }
}

@SpringBootTest
class SomeDbTest extends PostgresSingleton {
    @Autowired
    private UserDao userDao;

    @Test
    void userCountIsEqualTo2() {
        var count = userDao.count();
        assertThat(count).isEqualTo(2);
    }
}

@JdbcTest
@AutoConfigureTestDatabase(replace = NONE)
@Import(UserDao.class)
@Transactional(propagation = NOT_SUPPORTED)
class AnotherDbTest extends PostgresSingleton {
    @Autowired
    private UserDao userDao;

    @Test
    void userCountIsEqualTo2() {
        var count = userDao.count();
        assertThat(count).isEqualTo(2);
    }
}
