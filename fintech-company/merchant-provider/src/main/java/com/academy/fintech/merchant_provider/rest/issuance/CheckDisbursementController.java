package com.academy.fintech.merchant_provider.rest.issuance;

import com.academy.fintech.merchant_provider.core.service.disbursement.DisbursementMockServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/merchant-provider/check_disbursement")
public class CheckDisbursementController {

    private final DisbursementMockServer disbursementMockServer;

    @PostMapping
    public ResponseEntity<CheckDisbursementResponse> checkDisbursement(@RequestBody CheckDisbursementRequest request) {
        return disbursementMockServer.checkDisbursement(request);
    }
}
