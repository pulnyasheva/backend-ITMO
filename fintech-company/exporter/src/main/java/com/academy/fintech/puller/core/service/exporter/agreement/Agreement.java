package com.academy.fintech.puller.core.service.exporter.agreement;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
