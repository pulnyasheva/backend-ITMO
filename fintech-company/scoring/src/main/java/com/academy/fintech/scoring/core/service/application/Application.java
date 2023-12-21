package com.academy.fintech.scoring.core.service.application;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Заяка для сервиса scoring
 */
@Builder
public record Application(String clientId,
                          int clientSalary,
                          int loanTerm,
                          int disbursementAmount,
                          BigDecimal interest) {
}
