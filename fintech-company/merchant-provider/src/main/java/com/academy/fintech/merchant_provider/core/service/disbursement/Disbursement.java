package com.academy.fintech.merchant_provider.core.service.disbursement;

import lombok.Builder;


@Builder
public record Disbursement(String clientEmail,
                           String agreementId,
                           int amount) {
}
