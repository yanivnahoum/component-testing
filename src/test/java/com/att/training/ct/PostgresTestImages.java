package com.att.training.ct;

import org.testcontainers.utility.DockerImageName;

public class PostgresTestImages {
    public static final DockerImageName DEFAULT_IMAGE = DockerImageName.parse("postgres:14.9");
}
