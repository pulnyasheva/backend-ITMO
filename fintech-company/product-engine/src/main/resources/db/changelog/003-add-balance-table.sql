CREATE TABLE balance
(
    agreement_id VARCHAR,
    balance_type VARCHAR CHECK (balance_type IN ('CLIENT', 'OVERDUE')),
    amount       DECIMAL,
    PRIMARY KEY (agreement_id, balance_type),
    FOREIGN KEY (agreement_id) REFERENCES agreement (id)
);
