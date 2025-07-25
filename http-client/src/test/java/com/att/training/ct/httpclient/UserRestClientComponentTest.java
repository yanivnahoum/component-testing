package com.att.training.ct.httpclient;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.TestSocketUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRestClientComponentTest {
    private static final int AVAILABLE_PORT = TestSocketUtils.findAvailableTcpPort();
    @AutoClose
    private MockWebServer mockWebServer;
    @Autowired
    private UserClient userClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(AVAILABLE_PORT);
    }

    @DynamicPropertySource
    static void addProperties(DynamicPropertyRegistry registry) {
        registry.add("app.user.base-url", () -> "http://localhost:%d".formatted(AVAILABLE_PORT));
    }

    @Order(1)
    @Test
    @SuppressWarnings("java:S2699")
    void givenUserJane_leaveResponseQueued() {
        mockWebServer.enqueue(new MockResponse.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body("""
                        {
                            "id": 2,
                            "firstName": "Mary",
                            "lastName": "Smith"
                        }
                        """)
                .build()
        );

        // We then run some code that throws an exception or fails to dequeue the response
    }

    @Order(2)
    @Test
    void givenUserJohn_whenGetUser_thenReturnJohn() {
        mockWebServer.enqueue(new MockResponse.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
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
}
