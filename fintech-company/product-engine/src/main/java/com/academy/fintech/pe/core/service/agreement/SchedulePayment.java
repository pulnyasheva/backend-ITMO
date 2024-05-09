package com.academy.fintech.pe.core.service.agreement;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class SchedulePayment {

    private String scheduleId;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private BigDecimal periodPayment;
    private BigDecimal interestPayment;
    private BigDecimal principalPayment;
    private int periodNumber;
}
