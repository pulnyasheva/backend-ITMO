package com.academy.fintech.pe.core.service.agreement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class Balance {
    private String agreementId;
    private BalanceType type;
    private BigDecimal amount;
}
