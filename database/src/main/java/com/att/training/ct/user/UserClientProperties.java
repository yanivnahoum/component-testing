package com.att.training.ct.user;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("app.user")
@Validated
public record UserClientProperties(@NotEmpty String baseUrl) {}
