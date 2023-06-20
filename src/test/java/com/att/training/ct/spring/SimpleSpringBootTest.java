package com.att.training.ct.spring;

import com.att.training.ct.PostgresTestImages;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("assumes a database is available as configured in spring properties")
@SpringBootTest
class SimpleSpringBootTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void select1FromDb() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(result).isOne();
    }
}

@Disabled("since the Spring context knows nothing about the database started below")
@SpringBootTest
class LocalContainerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void startDbInTest() {
        try (var postgres = new PostgreSQLContainer<>(PostgresTestImages.DEFAULT_IMAGE)) {
            postgres.start();
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertThat(result).isOne();
        }
    }
}
