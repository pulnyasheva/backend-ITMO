package com.academy.fintech.pg.core.merchant_provider.client.rest;

import lombok.Builder;

@Builder
public record DisbursementResponse(String paymentId) {
}
