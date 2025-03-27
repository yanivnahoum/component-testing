package com.att.training.ct.httpclient;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class ProgrammaticWireMockUserRestClientTest {
    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().notifier(new Slf4jNotifier(true)))
            .configureStaticDsl(true)
            .build();
    private UserClient userClient;

    @BeforeEach
    void setUp() {
        UserClientProperties userClientProperties = new UserClientProperties(
                wireMock.getRuntimeInfo().getHttpBaseUrl()
        );
        userClient = new UserClient(RestClient.builder(), userClientProperties);
    }

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
