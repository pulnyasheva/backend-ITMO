package com.academy.fintech.dwh.core.service.conservation.agreement;

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
                        LocalDate nextPaymentDate,
                        LocalDate updatedAt
) {
}
