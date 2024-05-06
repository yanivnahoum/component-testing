package com.att.training.ct.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("SqlNoDataSourceInspection")
@Component
@RequiredArgsConstructor
public class UserDao {
    private final UserRowMapper userRowMapper = new UserRowMapper();
    private final JdbcClient jdbcClient;

    public int count() {
        var result = jdbcClient.sql("SELECT COUNT(1) FROM app.users")
                .query(Integer.class)
                .single();
        return requireNonNull(result);
    }

    public List<User> findAll() {
        return jdbcClient.sql("SELECT id, firstName, lastName FROM app.users")
                .query(userRowMapper)
                .list();
    }

    public void save(User user) {
        jdbcClient.sql("""
                        INSERT INTO app.users (id, firstName, lastName)
                        VALUES (?, ?, ?)
                        """)
                .param(user.id())
                .param(user.firstName())
                .param(user.lastName())
                .update();
    }


    public int delete(long id) {
        return jdbcClient.sql("DELETE FROM app.users WHERE id = ?")
                .param(id)
                .update();
    }
}

