package com.academy.fintech.origination.core.service.application;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Заявка для scoring
 */
@Builder
public record ScoringApplication(String clientId,
                                 int clientSalary,
                                 int loanTerm,
                                 int disbursementAmount,
                                 BigDecimal interest) {

}
