package com.att.training.ct.spring;

import com.att.training.ct.user.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.att.training.ct.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestContainersSingletonTest {
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE)
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("pwd")
            .withInitScript("db/init.sql");


    @Autowired
    private UserDao userDao;

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void registerDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void startDbBeforeSpringContext() {
        var count = userDao.count();
        assertThat(count).isEqualTo(2);
    }
}
