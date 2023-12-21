CREATE TABLE client
(
    id         VARCHAR PRIMARY KEY,
    first_name VARCHAR,
    last_name  VARCHAR,
    email      VARCHAR,
    salary     INT
);

CREATE TABLE application
(
    id                            VARCHAR PRIMARY KEY,
    client_id                     VARCHAR,
    requested_disbursement_amount INT,
    status                        VARCHAR CHECK ( status IN ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED') ),
    FOREIGN KEY (client_id) REFERENCES client (id)
);

CREATE UNIQUE INDEX idx_unique_email ON client (email);
