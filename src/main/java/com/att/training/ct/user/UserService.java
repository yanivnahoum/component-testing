package com.att.training.ct.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Comparator.comparingLong;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<User> findAll() {
        List<User> users = userDao.findAll();
        return switch (users.size()) {
            case 0 -> throw new NoUsersFoundException();
            case 1 -> users;
            default -> multipleUsersFound(users);
        };
    }

    private List<User> multipleUsersFound(List<User> users) {
        return users.stream()
                .sorted(comparingLong(User::id).reversed())
                .filter(byDistinctName())
                .toList();
    }

    private Predicate<User> byDistinctName() {
        record SimpleUser(String firstName, String lastName) {}
        Set<SimpleUser> uniqueUsers = new HashSet<>();
        return user -> {
            var simpleUser = new SimpleUser(user.firstName().toLowerCase(), user.lastName().toLowerCase());
            return uniqueUsers.add(simpleUser);
        };
    }
}
