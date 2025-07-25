package com.att.training.ct.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class UserRestClientTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @AutoClose
    private MockWebServer mockWebServer;
    private UserClient userClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        UserClientProperties userClientProperties = new UserClientProperties(
                mockWebServer.url("/").toString()
        );
        userClient = new UserClient(RestClient.builder(), userClientProperties);
    }

    @Test
    void givenUserJohn_whenGetUser_thenReturnJohn() {
        mockWebServer.enqueue(new MockResponse.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .body("""
                        {
                            "id": 1,
                            "firstName": "John",
                            "lastName": "Doe"
                        }
                        """)
                .build()
        );

        var user = userClient.get(1);

        assertThat(user).isEqualTo(new User(1, "John", "Doe"));
    }

    @Test
    void givenUserJohnDoe_whenPatchUser_thenMakeTheRightHttpRequest() throws InterruptedException, JsonProcessingException {
        var mockResponse = new MockResponse.Builder()
                .code(NO_CONTENT.value())
                .build();
        mockWebServer.enqueue(mockResponse);
        var patchedUser = new User(1, "John", "Smith");

        userClient.update(patchedUser);

        var recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getUrl().encodedPath()).isEqualTo("/users");
        assertThat(recordedRequest.getMethod()).isEqualTo(PATCH.name());
        assertThat(recordedRequest.getHeaders().get(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON_VALUE);
        assertThat(recordedRequest.getBody()).isNotNull()
                .extracting(ByteString::utf8).isEqualTo(objectMapper.writeValueAsString(patchedUser));
    }

    @Test
    void givenUserJohn_whenGetUser_thenReturnJohn_usingDispatcher() {
        mockWebServer.setDispatcher(new SimpleDispatcher());

        var user = userClient.get(1);

        assertThat(user).isEqualTo(new User(1, "John", "Doe"));
    }

    @Test
    void givenUserJohn_whenUpdateAndGet_thenReturnPatchedUser() {
        mockWebServer.setDispatcher(new UserServiceDispatcher());
        var patchedUser = new User(1, "John", "Smith");

        var user = userClient.updateAndGet(patchedUser);

        assertThat(user).isEqualTo(patchedUser);
    }

    private static class SimpleDispatcher extends Dispatcher {
        @Override
        public @NonNull MockResponse dispatch(@NonNull RecordedRequest recordedRequest) {
            return new MockResponse.Builder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .body("""
                            {
                                "id": 1,
                                "firstName": "John",
                                "lastName": "Doe"
                            }
                            """)
                    .build();
        }
    }

    private static class UserServiceDispatcher extends Dispatcher {
        private static final MockResponse NOT_FOUND_RESPONSE = new MockResponse.Builder()
                .code(NOT_FOUND.value())
                .build();
        private User user1 = new User(1, "John", "Doe");

        @Override
        public @NonNull MockResponse dispatch(@NonNull RecordedRequest recordedRequest) {
            return switch (recordedRequest.getUrl().encodedPath()) {
                case "/users/1" -> buildGetUser1Response();
                case "/users" -> buildUsersResponse(recordedRequest);
                default -> NOT_FOUND_RESPONSE;
            };
        }

        @SneakyThrows(JsonProcessingException.class)
        private @NotNull MockResponse buildGetUser1Response() {
            return new MockResponse.Builder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(user1))
                    .build();
        }

        private MockResponse buildUsersResponse(RecordedRequest recordedRequest) {
            MockResponse mockResponse = NOT_FOUND_RESPONSE;
            String httpMethodName = recordedRequest.getMethod();
            if (PATCH.matches(httpMethodName)) {
                tryUpdateUser1(recordedRequest);
                mockResponse = new MockResponse.Builder()
                        .code(NO_CONTENT.value())
                        .build();
            } else if (GET.matches(httpMethodName)) {
                mockResponse = new MockResponse.Builder().build();
            }
            return mockResponse;
        }

        @SneakyThrows(JsonProcessingException.class)
        private void tryUpdateUser1(RecordedRequest recordedRequest) {
            String body = requireNonNull(recordedRequest.getBody(), "Request body is null").utf8();
            User userFromBody = objectMapper.readValue(body, User.class);
            if (userFromBody.id() == 1) {
                user1 = userFromBody;
            }
        }
    }
}
