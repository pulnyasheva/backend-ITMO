package com.academy.fintech.merchant_provider.rest.issuance;

import com.academy.fintech.merchant_provider.core.service.disbursement.Disbursement;
import com.academy.fintech.merchant_provider.core.service.disbursement.DisbursementMockServer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/merchant-provider/disbursement")
public class DisbursementController {

    private final DisbursementMockServer disbursementMockServer;

    @PostMapping
    public DisbursementResponse disburse(@RequestBody DisbursementRequest request) {
        return disbursementMockServer.disburse(mapRequestToPayment(request));
    }

    private Disbursement mapRequestToPayment(DisbursementRequest request) {
        return Disbursement.builder()
                .clientEmail(request.clientEmail())
                .agreementId(request.agreementId())
                .amount(request.amountDisbursement())
                .build();
    }
}
