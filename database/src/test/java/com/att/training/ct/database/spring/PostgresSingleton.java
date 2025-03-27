package com.att.training.ct.database.spring;

import com.att.training.ct.database.UserDao;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.att.training.ct.database.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

public abstract class PostgresSingleton {
    @SuppressWarnings("resource")
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE)
            .withInitScript("db/init.sql")
            .withReuse(false);

    static {
        postgres.start();
    }
}

@Disabled("Depends on the 2 users from init script")
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

@Disabled("Depends on the 2 users from init script")
@JdbcTest
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
