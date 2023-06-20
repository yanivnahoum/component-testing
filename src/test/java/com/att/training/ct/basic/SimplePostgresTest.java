package com.att.training.ct.basic;

import com.att.training.ct.PostgresTestImages;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

class SimplePostgresTest {
    @Test
    void whenPostgresStart_thenDbIsReachable_andThenItShutsDown() {
        try (var postgres = new PostgreSQLContainer<>(PostgresTestImages.DEFAULT_IMAGE)) {
            postgres.start();
            var jdbcTemplate = new JdbcTemplate(buildDataSource(postgres));
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertThat(result).isOne();
        }
    }

    protected DataSource buildDataSource(JdbcDatabaseContainer<?> container) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }
}
