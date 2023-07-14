DROP TABLE IF EXISTS users, categories, locations, events, compilations,
compilations_events, participation_requests, rating_events;

CREATE TABLE IF NOT EXISTS users (
    user_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(250) NOT NULL,
    email        VARCHAR(254) NOT NULL UNIQUE,
    CONSTRAINT user_pk PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS categories (
    cat_id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(64) NOT NULL UNIQUE,
    CONSTRAINT category_pk PRIMARY KEY (cat_id)
);

CREATE TABLE IF NOT EXISTS locations (
    loc_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat         FLOAT,
    lon         FLOAT,
    CONSTRAINT location_pk PRIMARY KEY (loc_id)
);

CREATE table IF NOT EXISTS events (
    event_id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation          VARCHAR(2000) NOT NULL,
    category_id         BIGINT REFERENCES categories (cat_id) ON DELETE CASCADE,
    created_on          TIMESTAMP without time zone,
    description         VARCHAR(7000) NOT NULL,
    event_date          TIMESTAMP without time zone NOT NULL,
    initiator_id        BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    location_id         BIGINT REFERENCES locations (loc_id) ON DELETE CASCADE,
    paid                BOOLEAN NOT NULL,
    participant_limit   INTEGER NOT NULL,
    published_on        TIMESTAMP without time zone,
    request_moderation  BOOLEAN NOT NULL,
    state               VARCHAR(32),
    title               VARCHAR(120) NOT NULL,
    views               BIGINT,
    rating              BIGINT,
    CONSTRAINT event_pk PRIMARY KEY (event_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title               VARCHAR(256) NOT NULL,
    pinned              BOOLEAN,
    CONSTRAINT compilation_pk PRIMARY KEY (compilation_id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    event_id              BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    compilation_id        BIGINT REFERENCES compilations (compilation_id) ON DELETE CASCADE,
    PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS participation_requests (
    request_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created          TIMESTAMP without time zone,
    event_id         BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    requester_id     BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    status           VARCHAR(32),
    CONSTRAINT participation_request_pk PRIMARY KEY (request_id)
);

CREATE TABLE IF NOT EXISTS rating_events (
    rating_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id     BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    event_id    BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    rating      INTEGER NOT NULL,
    CONSTRAINT rating_events_pk PRIMARY KEY (rating_id)
);
