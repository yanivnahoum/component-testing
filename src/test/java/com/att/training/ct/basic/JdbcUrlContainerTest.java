package com.att.training.ct.basic;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcUrlContainerTest {
    @Test
    void whenPostgresStart_thenDbIsReachable_andThenItShutsDown() {
        try (var datasource = buildDataSource()) {
            var jdbcTemplate = new JdbcTemplate(datasource);
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertThat(result).isOne();
        }
    }

    protected HikariDataSource buildDataSource() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:tc:postgresql:14.9:///test?TC_INITSCRIPT=db/init.sql");
        hikariConfig.setUsername("test");
        hikariConfig.setPassword("test");
        return new HikariDataSource(hikariConfig);
    }
}
