package com.academy.fintech.origination.core.pe.client.activation;

import com.academy.fintech.disbursement.Date;
import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.origination.core.pe.client.activation.grpc.DisbursementGrpcClient;
import com.academy.fintech.origination.core.service.agreement.ApplicationActivation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisbursementClientService {

    private final DisbursementGrpcClient disbursementGrpcClient;

    public String disbursement(ApplicationActivation application) {
        return disbursementGrpcClient.disbursement(mapApplicationToRequest(application)).getScheduleId();
    }

    private DisbursementRequest mapApplicationToRequest(ApplicationActivation application) {
        return DisbursementRequest.newBuilder()
                .setAgreementId(application.agreementId())
                .setDisbursementDate(Date.newBuilder()
                        .setDay(application.disbursementDate().getDayOfMonth())
                        .setMonth(application.disbursementDate().getMonthValue())
                        .setYear(application.disbursementDate().getYear())
                        .build())
                .build();
    }
}
