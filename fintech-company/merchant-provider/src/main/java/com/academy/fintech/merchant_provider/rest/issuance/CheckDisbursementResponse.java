package com.academy.fintech.merchant_provider.rest.issuance;

import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CheckDisbursementResponse(boolean readyDisbursement,
                                        @Nullable LocalDate dateDisbursement) {
}
