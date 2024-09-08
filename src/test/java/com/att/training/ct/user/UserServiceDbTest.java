package com.att.training.ct.user;

import com.att.training.ct.spring.PostgresSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@SpringBootTest
class UserServiceDbTest extends PostgresSingleton {
    @Autowired
    private UserService userService;
    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        deleteFromTables(jdbcClient, "app.users");
    }

    @Test
    void givenNoUsers_whenFindAll_thenExceptionIsThrown() {
        assertThatExceptionOfType(NoUsersFoundException.class)
                .isThrownBy(() -> userService.findAll());
    }

    @Test
    @Sql(statements = "DELETE FROM app.users", executionPhase = AFTER_TEST_METHOD)
    void givenMultipleUniqueUsers_whenFindAll_thenReturnAll() {
        // given
        User john = new User(1, "John", "Doe");
        User mary = new User(2, "Mary", "Smith");
        User alice = new User(3, "Alice", "Smith");
        jdbcClient.sql("""
                        INSERT INTO app.users (id, firstName, lastName)
                        VALUES (1, 'John', 'Doe'),
                               (2, 'Mary', 'Smith'),
                               (3, 'Alice', 'Smith')
                        """)
                .update();
        // when
        var users = userService.findAll();
        // then
        assertThat(users).containsExactlyInAnyOrder(john, mary, alice);
    }

}
