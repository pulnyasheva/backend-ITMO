package com.academy.fintech.pg.core.origination;

import com.academy.fintech.create_agreement.ActivationRequest;
import com.academy.fintech.create_agreement.Date;
import com.academy.fintech.pg.core.origination.grpc.ActivationGrpcClient;
import com.academy.fintech.pg.core.service.agreement.ApplicationActivation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationClientServer {

    private final ActivationGrpcClient activationGrpcClient;

    public boolean activate(ApplicationActivation application) {
        return activationGrpcClient.activate(mapApplicationToRequest(application)).getSuccessfully();
    }

    private ActivationRequest mapApplicationToRequest(ApplicationActivation application) {
        return ActivationRequest.newBuilder()
                .setAgreementId(application.agreementId())
                .setDisbursementDate(Date.newBuilder()
                        .setDay(application.disbursementDate().getDayOfMonth())
                        .setMonth(application.disbursementDate().getMonthValue())
                        .setYear(application.disbursementDate().getYear())
                        .build())
                .build();
    }
}
