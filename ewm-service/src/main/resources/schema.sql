DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name         VARCHAR(64) NOT NULL,
    email        VARCHAR(64) NOT NULL UNIQUE,
    CONSTRAINT user_pk PRIMARY KEY (id)
);