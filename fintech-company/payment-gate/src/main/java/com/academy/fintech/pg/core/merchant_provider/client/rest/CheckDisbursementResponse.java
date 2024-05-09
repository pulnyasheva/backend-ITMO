package com.academy.fintech.pg.core.merchant_provider.client.rest;

import lombok.Builder;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Builder
public record CheckDisbursementResponse(boolean readyDisbursement,
                                        @Nullable LocalDate dateDisbursement) {
}
