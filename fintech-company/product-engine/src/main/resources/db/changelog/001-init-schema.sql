CREATE TABLE product
(
    code                   VARCHAR PRIMARY KEY,
    min_loan_term          INT,
    max_loan_term          INT,
    min_principal_amount   DECIMAL,
    max_principal_amount   DECIMAL,
    min_interest           DECIMAL,
    max_interest           DECIMAL,
    min_origination_amount DECIMAL,
    max_origination_amount DECIMAL
);

CREATE TABLE agreement
(
    id                 VARCHAR PRIMARY KEY,
    product_code       VARCHAR,
    client_id          VARCHAR,
    interest           DECIMAL,
    term               INT,
    principal_amount   DECIMAL,
    origination_amount DECIMAL,
    status             VARCHAR CHECK ( status IN ('NEW', 'ACTIVE', 'CLOSED') ),
    disbursement_date  DATE,
    next_payment_date  DATE,
    FOREIGN KEY (product_code) REFERENCES product (code)
);

CREATE TABLE payment_schedule
(
    id           VARCHAR PRIMARY KEY,
    agreement_id VARCHAR,
    version      INT,
    FOREIGN KEY (agreement_id) REFERENCES agreement (id)
);

CREATE TABLE payment_schedule_payment
(
    payment_schedule_id VARCHAR,
    status              VARCHAR CHECK ( status IN ('PAID', 'OVERDUE', 'FUTURE') ),
    payment_date        DATE,
    period_payment      DECIMAL,
    interest_payment    DECIMAL,
    principal_payment   DECIMAL,
    period_number       INT,
    FOREIGN KEY (payment_schedule_id) REFERENCES payment_schedule (id)
);