CREATE SCHEMA IF NOT EXISTS app;

CREATE TABLE IF NOT EXISTS app.users
(
    id
    integer
    PRIMARY
    KEY,
    firstName
    varchar
(
    30
) NOT NULL,
    lastName varchar
(
    30
) NOT NULL
);

TRUNCATE TABLE app.users;

INSERT INTO app.users (id, firstName, lastName)
VALUES (1, 'John', 'Doe'),
       (2, 'Mary', 'Smith');
