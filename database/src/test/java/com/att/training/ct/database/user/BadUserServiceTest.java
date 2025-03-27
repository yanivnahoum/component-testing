package com.att.training.ct.database.user;

import com.att.training.ct.user.NoUsersFoundException;
import com.att.training.ct.user.User;
import com.att.training.ct.user.UserDao;
import com.att.training.ct.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadUserServiceTest {
    @Mock(answer = RETURNS_DEEP_STUBS)
    private JdbcClient jdbcClient;
    @Mock
    private JdbcClient.StatementSpec statementSpec;
    @Mock
    private JdbcClient.MappedQuerySpec<User> mappedQuerySpec;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new UserDao(jdbcClient));
        lenient().when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        lenient().when(statementSpec.query(anyUserRowMapper())).thenReturn(mappedQuerySpec);
    }

    @Test
    void givenNoUsers_whenFindAll_thenExceptionIsThrown() {
        assertThatExceptionOfType(NoUsersFoundException.class)
                .isThrownBy(() -> userService.findAll());
    }

    @Test
    void givenSingleUsers_whenFindAll_thenReturnIt() {
        // given
        User john = new User(1, "John", "Doe");
        when(jdbcClient.sql(anyString()).query(anyUserRowMapper()).list())
                .thenReturn(List.of(john));
        // when
        var users = userService.findAll();
        // then
        assertThat(users).containsExactly(john);
    }

    @Test
    void givenSingleUsers_whenFindAll_thenReturnIt2() {
        // given
        User john = new User(1, "John", "Doe");
        when(mappedQuerySpec.list()).thenReturn(List.of(john));
        // when
        var users = userService.findAll();
        // then
        assertThat(users).containsExactly(john);
    }

    @Test
    void givenMultipleUniqueUsers_whenFindAll_thenReturnAll() {
        // given
        User john = new User(1, "John", "Doe");
        User mary = new User(2, "Mary", "Smith");
        User alice = new User(3, "Alice", "Smith");
        when(jdbcClient.sql(anyString()).query(anyUserRowMapper()).list())
                .thenReturn(List.of(john, mary, alice));
        // when
        var users = userService.findAll();
        // then
        assertThat(users).containsExactlyInAnyOrder(john, mary, alice);
    }

    @Test
    void givenMultipleNonUniqueUsers_whenFindAll_thenReturnGreatestId() {
        // given
        User john = new User(1, "John", "Doe");
        User mary1 = new User(2, "Mary", "Smith");
        User mary2 = new User(3, "Mary", "Smith");
        when(jdbcClient.sql(anyString()).query(anyUserRowMapper()).list())
                .thenReturn(List.of(john, mary1, mary2));
        // when
        var users = userService.findAll();
        // then
        assertThat(users).containsExactlyInAnyOrder(john, mary2);
    }

    private static RowMapper<User> anyUserRowMapper() {
        return any();
    }
}
