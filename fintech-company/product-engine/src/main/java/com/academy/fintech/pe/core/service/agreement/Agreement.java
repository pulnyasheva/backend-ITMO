package com.academy.fintech.pe.core.service.agreement;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record Agreement(String id,
                        String productCode,
                        String clientId,
                        BigDecimal interest,
                        int loanTerm,
                        BigDecimal principalAmount,
                        BigDecimal originationAmount,
                        AgreementStatus status,
                        LocalDate disbursementDate,
                        LocalDate nextPaymentDate
) {
}
