package com.academy.fintech.pg.core.service.disbursement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Disbursement {

    private String id;
    private String clientEmail;
    private String agreementId;
    private int amount;
}
