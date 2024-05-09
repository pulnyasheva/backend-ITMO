package com.academy.fintech.origination.core.service.disbursement;

import lombok.Builder;

@Builder
public record Disbursement(String clientEmail, String agreementId, int amount) {
}
