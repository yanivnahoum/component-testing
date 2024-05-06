package com.att.training.ct.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDao);
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
        when(userDao.findAll()).thenReturn(List.of(john));
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
        when(userDao.findAll()).thenReturn(List.of(john, mary, alice));
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
        when(userDao.findAll()).thenReturn(List.of(john, mary1, mary2));
        // when
        var users = userService.findAll();
        // then
        assertThat(users).containsExactlyInAnyOrder(john, mary2);
    }
}