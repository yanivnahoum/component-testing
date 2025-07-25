package com.att.training.ct.httpclient;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BadUserRestClientComponentTest {
    @AutoClose
    private static MockWebServer mockWebServer;
    @Autowired
    private UserClient userClient;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void addProperties(DynamicPropertyRegistry registry) {
        registry.add("app.user.base-url", () -> mockWebServer.url("/").toString());
    }

    @Order(1)
    @Test
    @SuppressWarnings("java:S2699")
    void givenUserMary_leaveResponseQueued() {
        mockWebServer.enqueue(new MockResponse.Builder()
                .setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
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

        // We get the wrong response here, left over from the previous test!
        assertThat(user).isNotEqualTo(new User(1, "John", "Doe"))
                .isEqualTo(new User(2, "Mary", "Smith"));
    }
}
