package com.att.training.ct.webclients;

import com.att.training.ct.user.User;
import com.att.training.ct.user.UserClient;
import com.att.training.ct.user.UserClientProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
class AnotherUserRestClientTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private UserClient userClient;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wmRuntimeInfo) {
        UserClientProperties userClientProperties = new UserClientProperties(
                wmRuntimeInfo.getHttpBaseUrl()
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

    @Test
    void givenUserJohnDoe_whenPatchUser_thenMakeTheRightHttpRequest() throws JsonProcessingException {
        var patchedUser = new User(1, "John", "Smith");

        stubFor(patch("/users")
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(patchedUser)))
                .willReturn(noContent()));

        assertThatNoException().isThrownBy(() -> userClient.update(patchedUser));
    }

    @Test
    void givenUserJohnDoe_whenPatchUser_thenVerifyTheRightHttpRequestWasMade() throws JsonProcessingException {
        stubFor(patch("/users").willReturn(noContent()));
        var patchedUser = new User(1, "John", "Smith");

        userClient.update(patchedUser);

        verify(patchRequestedFor(urlEqualTo("/users"))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(patchedUser))));
    }
}
