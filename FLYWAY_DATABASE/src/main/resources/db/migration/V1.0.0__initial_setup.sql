CREATE SCHEMA IF NOT EXISTS TEST_FLYWAY;

CREATE TABLE IF NOT EXISTS TEST_FLYWAY.TEST_TABLE
(
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(50)
);