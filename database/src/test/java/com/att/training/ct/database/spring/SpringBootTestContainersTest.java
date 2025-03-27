package com.att.training.ct.database.spring;

import com.att.training.ct.database.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.att.training.ct.database.PostgresTestImages.DEFAULT_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.sql.init.mode=always")
@Testcontainers
class SpringBootTestContainersTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DEFAULT_IMAGE);

    @Autowired
    private UserDao userDao;

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
