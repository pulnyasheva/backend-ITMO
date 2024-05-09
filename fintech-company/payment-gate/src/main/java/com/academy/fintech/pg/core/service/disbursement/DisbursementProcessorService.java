package com.academy.fintech.pg.core.service.disbursement;

import com.academy.fintech.disbursement_processor.DisbursementProcessorRequest;
import com.academy.fintech.disbursement_processor.DisbursementProcessorResponse;
import com.academy.fintech.pg.core.merchant_provider.client.DisbursementClientServer;
import com.academy.fintech.pg.core.service.disbursement.db.DisbursementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisbursementProcessorService {

    private final DisbursementClientServer disbursementClientServer;
    private final DisbursementService disbursementService;
    private final CheckDisbursementService checkDisbursementService;

    /**
     * Запрошивает выдачу платежа и вызывает начало проверок выдачи платежа
     */
    public DisbursementProcessorResponse disburse(DisbursementProcessorRequest request) {
        Disbursement payment = mapRequestToDisbursement(request);
        String paymentId = disbursementClientServer.disburse(payment);
        payment.setId(paymentId);

        disbursementService.add(payment);
        checkDisbursementService.checkDisbursement(payment);

        return DisbursementProcessorResponse.newBuilder()
                .setAccepted(true)
                .build();
    }

    private Disbursement mapRequestToDisbursement(DisbursementProcessorRequest request) {
        return Disbursement.builder()
                .clientEmail(request.getClientEmail())
                .agreementId(request.getAgreementId())
                .amount(request.getAmountDisbursement())
                .build();
    }
}
