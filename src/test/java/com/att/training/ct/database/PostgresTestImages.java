package com.att.training.ct.database;

import org.testcontainers.utility.DockerImageName;

public class PostgresTestImages {
    public static final DockerImageName DEFAULT_IMAGE = DockerImageName.parse("postgres:16.4");
}
