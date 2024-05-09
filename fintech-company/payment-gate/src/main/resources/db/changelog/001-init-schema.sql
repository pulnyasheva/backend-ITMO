CREATE TABLE issue_payment
(
    id           VARCHAR PRIMARY KEY,
    agreement_id VARCHAR,
    client_email VARCHAR,
    amount       VARCHAR,
    status       VARCHAR CHECK ( status IN ('READY', 'WAIT', 'ERROR') ),
    FOREIGN KEY (agreement_id) REFERENCES agreement (id),
    FOREIGN KEY (client_email) REFERENCES client (email)
);
