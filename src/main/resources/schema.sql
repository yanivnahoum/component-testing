CREATE SCHEMA IF NOT EXISTS app;

CREATE TABLE IF NOT EXISTS app.users (
    id   integer PRIMARY KEY,
    name varchar(30) NOT NULL
);
