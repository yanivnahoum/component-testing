package com.att.training.ct.spring;

import com.att.training.ct.user.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.att.training.ct.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Slf4j
abstract class PostgresSingleton {
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE)
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("pwd")
            .withInitScript("db/init.sql");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void registerDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
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
class AnotherDbTest extends PostgresSingleton {
    @Autowired
    private UserDao userDao;

    @Test
    void userCountIsEqualTo2() {
        var count = userDao.count();
        assertThat(count).isEqualTo(2);
    }
}
