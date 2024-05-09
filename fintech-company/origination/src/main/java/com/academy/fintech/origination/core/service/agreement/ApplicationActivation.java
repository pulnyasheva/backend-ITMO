package com.academy.fintech.origination.core.service.agreement;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ApplicationActivation(String agreementId, LocalDate disbursementDate) {
}
