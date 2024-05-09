package com.academy.fintech.origination.core.service.agreement;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Agreement(String productCode,
                        String clientId,
                        BigDecimal interest,
                        int loanTerm,
                        BigDecimal originationAmount,
                        BigDecimal disbursementAmount
) {
}

