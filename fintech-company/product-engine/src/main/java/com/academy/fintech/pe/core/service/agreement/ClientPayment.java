package com.academy.fintech.pe.core.service.agreement;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ClientPayment(String agreementId, BigDecimal amountPayment) {
}
