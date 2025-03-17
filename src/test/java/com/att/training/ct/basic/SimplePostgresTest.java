package com.att.training.ct.basic;

import com.att.training.ct.PostgresTestImages;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

class SimplePostgresTest {
    @Test
    void whenPostgresStart_thenDbIsReachable_andThenItShutsDown() {
        try (var postgres = new PostgreSQLContainer<>(PostgresTestImages.DEFAULT_IMAGE);
             var datasource = buildDataSource(postgres)) {
            var jdbcClient = JdbcClient.create(datasource);
            int result = jdbcClient.sql("SELECT 1")
                    .query(Integer.class)
                    .single();
            assertThat(result).isOne();
        }
    }

    protected HikariDataSource buildDataSource(JdbcDatabaseContainer<?> container) {
        container.start();
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }
}
