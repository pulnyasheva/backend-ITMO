package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.create_schedule.CreateScheduleRequest;
import com.academy.fintech.create_schedule.CreateScheduleResponse;
import com.academy.fintech.pe.core.calculation.payment_schedule.Functions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ScoringCreationScheduleService {

    /**
     * Расчитывает регулярный платеж для scoring
     */
    public CreateScheduleResponse createSchedule(CreateScheduleRequest request) {
        final BigDecimal countPaymentInYear = new BigDecimal(12);
        final int countRound = 10000;

        BigDecimal interest = new BigDecimal(request.getInterest());
        BigDecimal rate = interest.divide(countPaymentInYear, countRound, RoundingMode.HALF_UP);

        return CreateScheduleResponse.newBuilder()
                .setPeriodPayment(
                        Functions.PMT(rate, request.getLoanTerm(), new BigDecimal(request.getDisbursementAmount()))
                                .toString())
                .build();
    }

}
