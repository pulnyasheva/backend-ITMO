package com.academy.fintech.pe.core.service.agreement;

import lombok.Builder;

import java.util.List;

@Builder
public record Schedule(String id,
                       String agreementId,
                       int version,
                       List<SchedulePayment> schedulePayments) {
}
