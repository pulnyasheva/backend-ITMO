CREATE TABLE application
(
    id                            VARCHAR,
    client_id                     VARCHAR,
    requested_disbursement_amount INT,
    status                        VARCHAR CHECK ( status IN ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED') ),
    updated_at                    TIMESTAMP,
    PRIMARY KEY (updated_at, id)
) PARTITION BY RANGE (updated_at);

SELECT partman.create_parent(
               p_parent_table := 'public.application',
               p_control := 'updated_at',
               p_type := 'native',
               p_interval := 'P1D',
               p_start_partition := '2024-05-01 00:00:00'::text,
               p_premake := 12
           );

UPDATE partman.part_config
SET infinite_time_partitions= true
WHERE parent_table in ('application');

CREATE TABLE agreement
(
    id                 VARCHAR,
    product_code       VARCHAR,
    client_id          VARCHAR,
    interest           DECIMAL,
    term               INT,
    principal_amount   DECIMAL,
    origination_amount DECIMAL,
    status             VARCHAR CHECK ( status IN ('NEW', 'ACTIVE', 'CLOSED') ),
    disbursement_date  DATE,
    next_payment_date  DATE,
    updated_at         TIMESTAMP,
    PRIMARY KEY (updated_at, id)
) PARTITION BY RANGE (updated_at);

SELECT partman.create_parent(
               p_parent_table := 'public.agreement',
               p_control := 'updated_at',
               p_type := 'native',
               p_interval := 'P1D',
               p_start_partition := '2024-05-01 00:00:00'::text,
               p_premake := 12
           );

UPDATE partman.part_config
SET infinite_time_partitions= true
WHERE parent_table in ('agreement');