package com.academy.fintech.scoring.core.pe.client;

import com.academy.fintech.create_schedule.CreateScheduleRequest;
import com.academy.fintech.scoring.core.pe.client.grpc.PEGrpcClient;
import com.academy.fintech.scoring.core.service.application.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class SchedulePEClientService {

    private final PEGrpcClient peGrpcClient;

    /**
     * Получение сумму регулярного платежа по желаемой сумме кредита.
     */
    public BigDecimal createSchedule(Application application) {
        CreateScheduleRequest request = mapApplicationToRequest(application);
        return new BigDecimal(peGrpcClient.createSchedule(request).getPeriodPayment());
    }

    private static CreateScheduleRequest mapApplicationToRequest(Application application) {
        return CreateScheduleRequest.newBuilder()
                .setLoanTerm(application.loanTerm())
                .setDisbursementAmount(String.valueOf(application.disbursementAmount()))
                .setInterest(application.interest().toString())
                .build();
    }

}
