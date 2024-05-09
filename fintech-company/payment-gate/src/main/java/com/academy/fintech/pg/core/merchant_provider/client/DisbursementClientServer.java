package com.academy.fintech.pg.core.merchant_provider.client;

import com.academy.fintech.pg.core.service.disbursement.Disbursement;
import com.academy.fintech.pg.core.merchant_provider.client.rest.DisbursementRequest;
import com.academy.fintech.pg.core.merchant_provider.client.rest.DisbursementRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisbursementClientServer {

    private final DisbursementRestClient disbursementRestClient;

    public String disburse(Disbursement payment) {
        return disbursementRestClient.disburse(mapPaymentToRequest(payment)).paymentId();
    }

    private DisbursementRequest mapPaymentToRequest(Disbursement payment) {
        return DisbursementRequest.builder()
                .clientEmail(payment.getClientEmail())
                .agreementId(payment.getAgreementId())
                .amountDisbursement(payment.getAmount())
                .build();
    }
}
