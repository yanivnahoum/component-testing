package com.att.training.ct.webclients;

import com.att.training.ct.user.User;
import com.att.training.ct.user.UserClient;
import com.att.training.ct.user.UserClientProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class UserRestClientTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
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

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void givenUserJohn_whenGetUser_thenReturnJohn() {
        mockWebServer.enqueue(new MockResponse()
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody("""
                        {
                            "id": 1,
                            "firstName": "John",
                            "lastName": "Doe"
                        }
                        """)
        );

        var user = userClient.get(1);

        assertThat(user).isEqualTo(new User(1, "John", "Doe"));
    }

    @Test
    void givenUserJohnDoe_whenPatchUser_thenMakeTheRightHttpRequest() throws InterruptedException, JsonProcessingException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(NO_CONTENT.value()));
        var patchedUser = new User(1, "John", "Smith");

        userClient.update(patchedUser);

        var recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath()).isEqualTo("/users");
        assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.PATCH.name());
        assertThat(recordedRequest.getHeader(CONTENT_TYPE)).isEqualTo(APPLICATION_JSON_VALUE);
        assertThat(recordedRequest.getBody().readUtf8()).isEqualTo(objectMapper.writeValueAsString(patchedUser));
    }
}
