package com.att.training.ct.basic;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcUrlContainerTest {
    @Test
    void whenPostgresStart_thenDbIsReachable_andThenItShutsDown() {
        try (var datasource = buildDataSource()) {
            var jdbcClient = JdbcClient.create(datasource);
            int result = jdbcClient.sql("SELECT 1")
                    .query(Integer.class)
                    .single();
            assertThat(result).isOne();
        }
    }

    protected HikariDataSource buildDataSource() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:tc:postgresql:16.4:///test?TC_INITSCRIPT=db/init.sql");
        hikariConfig.setUsername("test");
        hikariConfig.setPassword("test");
        return new HikariDataSource(hikariConfig);
    }
}
