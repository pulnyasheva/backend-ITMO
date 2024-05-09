package com.academy.fintech.merchant_provider.rest.issuance;

import lombok.Builder;

@Builder
public record DisbursementRequest(String clientEmail, String agreementId, int amountDisbursement) {
}
