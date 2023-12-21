package com.academy.fintech.pe.core.service.agreement;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record SchedulePayment(String scheduleId,
                              PaymentStatus status,
                              LocalDate paymentDate,
                              BigDecimal periodPayment,
                              BigDecimal interestPayment,
                              BigDecimal principalPayment,
                              int periodNumber) {
}
