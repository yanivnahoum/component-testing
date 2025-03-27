package com.att.training.ct.webclient;

import com.att.training.ct.user.User;
import com.att.training.ct.user.UserClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(
        baseUrlProperties = "app.user.base-url"
))
class WireMockUserRestClientComponentTest {
    @Autowired
    private UserClient userClient;

    @Test
    void givenUserJohn_whenGetUser_thenReturnJohn() {
        stubFor(get("/users/1").willReturn(
                ok().withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "id": 1,
                                    "firstName": "John",
                                    "lastName": "Doe"
                                }
                                """)
        ));

        var user = userClient.get(1);

        assertThat(user).isEqualTo(new User(1, "John", "Doe"));
    }
}
