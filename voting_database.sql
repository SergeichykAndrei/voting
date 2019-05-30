CREATE DATABASE voting_database;

CREATE SCHEMA voting_storage;

SET SEARCH_PATH TO voting_storage;

CREATE TABLE "user" (
  id       BIGSERIAL PRIMARY KEY,
  username CHARACTER VARYING(128) NOT NULL UNIQUE
);

CREATE TABLE voting (
  id     BIGSERIAL PRIMARY KEY,
  name   CHARACTER VARYING(256) NOT NULL,
  status CHARACTER VARYING(64)  NOT NULL
);

CREATE TABLE topic (
  id        BIGSERIAL PRIMARY KEY,
  name      CHARACTER VARYING(256)        NOT NULL,
  voting_id BIGINT REFERENCES voting (id) NOT NULL
);

CREATE TABLE user_topic (
  id       BIGSERIAL PRIMARY KEY,
  user_id  BIGINT REFERENCES "user" (id) NOT NULL,
  topic_id BIGINT REFERENCES topic (id)  NOT NULL
);