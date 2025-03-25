package com.att.training.ct.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class UserClient {
    private final RestClient restClient;

    public UserClient(RestClient.Builder restClientBuilder, UserClientProperties userClientProperties) {
        this.restClient = restClientBuilder
                .baseUrl(userClientProperties.baseUrl())
                .build();
    }

    public User get(long id) {
        return restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .body(User.class);
    }

    public List<User> getAll() {
        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void update(User user) {
        restClient.patch()
                .uri("/users")
                .contentType(APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();
    }

    public User updateAndGet(User user) {
        update(user);
        return get(user.id());
    }
}
