package com.academy.fintech.merchant_provider.rest.issuance;

import lombok.Builder;

@Builder
public record DisbursementResponse(String paymentId) {
}
