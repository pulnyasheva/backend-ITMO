package com.academy.fintech.pg.core.service.processing;

import lombok.Builder;

@Builder
public record ClientPayment(String agreementId, int amountPayment) {
}
