package com.academy.fintech.pg.core.merchant_provider.client;

import com.academy.fintech.pg.core.service.disbursement.Disbursement;
import com.academy.fintech.pg.core.service.disbursement.ReadyDisbursement;
import com.academy.fintech.pg.core.merchant_provider.client.rest.CheckDisbursementRequest;
import com.academy.fintech.pg.core.merchant_provider.client.rest.CheckDisbursementResponse;
import com.academy.fintech.pg.core.merchant_provider.client.rest.CheckDisbursementRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckDisbursementClientServer {

    private final CheckDisbursementRestClient checkDisbursementRestClient;

    public ReadyDisbursement checkIssuance(Disbursement payment) {
        return mapResponseToReady(checkDisbursementRestClient.checkDisbursement(mapPaymentToRequest(payment)));
    }

    private CheckDisbursementRequest mapPaymentToRequest(Disbursement payment) {
        return CheckDisbursementRequest.builder()
                .paymentId(payment.getId())
                .build();
    }

    private ReadyDisbursement mapResponseToReady(CheckDisbursementResponse response) {
        return ReadyDisbursement.builder()
                .ready(response.readyDisbursement())
                .dateDisbursement(response.dateDisbursement())
                .build();
    }
}
