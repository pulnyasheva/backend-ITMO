package com.academy.fintech.origination.core.pg.client;

import com.academy.fintech.disbursement_processor.DisbursementProcessorRequest;
import com.academy.fintech.origination.core.pg.client.grpc.DisbursementGrpcClient;
import com.academy.fintech.origination.core.service.disbursement.Disbursement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisbursementClientService {

    private final DisbursementGrpcClient disbursementGrpcClient;

    public boolean disburse(Disbursement disbursement) {
        return disbursementGrpcClient.disburse(mapDisbursementToRequest(disbursement)).getAccepted();
    }

    private DisbursementProcessorRequest mapDisbursementToRequest(Disbursement disbursement) {
        return DisbursementProcessorRequest.newBuilder()
                .setClientEmail(disbursement.clientEmail())
                .setAgreementId(disbursement.agreementId())
                .setAmountDisbursement(disbursement.amount())
                .build();
    }
}
