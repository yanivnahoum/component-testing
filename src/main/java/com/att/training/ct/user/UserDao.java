package com.att.training.ct.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public int count() {
        var result = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM app.users", Integer.class);
        return requireNonNull(result);
    }

}
