package com.att.training.ct.spring;

import com.att.training.ct.user.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.att.training.ct.PostgresTestImages.DEFAULT_IMAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

class PostgresContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE)
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("pwd")
            .withInitScript("db/init.sql");

    static {
        postgres.start();
    }

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        TestPropertyValues values = TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword()
        );
        values.applyTo(applicationContext);
    }
}

@SpringBootTest
@ContextConfiguration(initializers = PostgresContextInitializer.class)
class SomeOtherDbTest extends PostgresSingleton {
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
@WithPostgresContainer
class YetAnotherDbTest extends PostgresSingleton {
    @Autowired
    private UserDao userDao;

    @Test
    void userCountIsEqualTo2() {
        var count = userDao.count();
        assertThat(count).isEqualTo(2);
    }
}

@Target(TYPE)
@Retention(RUNTIME)
@ContextConfiguration(initializers = PostgresContextInitializer.class)
@interface WithPostgresContainer {}
