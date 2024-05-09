CREATE TABLE export_types
(
    code        VARCHAR,
    description VARCHAR NOT NULL,
    retry_count INT     NOT NULL DEFAULT 3,

    PRIMARY KEY (code)
);

INSERT INTO export_types (code, description)
VALUES ('AGREEMENT', 'The client''s agreement'),
       ('APPLICATION', 'Application for a loan or other banking transaction');

----

CREATE SEQUENCE s_export_tasks;

CREATE TABLE export_tasks
(
    id            BIGINT                            DEFAULT nextval('s_export_tasks'),
    type          VARCHAR                  NOT NULL,
    status        VARCHAR                  NOT NULL,
    string_key    VARCHAR,
    number_key    BIGINT,
    retries_count INT                      NOT NULL DEFAULT 0,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT clock_timestamp(),

    PRIMARY KEY (id),
    CONSTRAINT export_tasks_fk1 FOREIGN KEY (type) REFERENCES export_types (code),
    CONSTRAINT export_tasks_ch1 CHECK (status IN ('NEW', 'PROCESSING', 'SUCCESS', 'ERROR'))
);
