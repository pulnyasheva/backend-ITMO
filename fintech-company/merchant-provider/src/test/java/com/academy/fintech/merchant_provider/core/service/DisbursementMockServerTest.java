package com.academy.fintech.merchant_provider.core.service;

import com.academy.fintech.merchant_provider.core.service.disbursement.Disbursement;
import com.academy.fintech.merchant_provider.core.service.disbursement.DisbursementMockServer;
import com.academy.fintech.merchant_provider.rest.issuance.CheckDisbursementRequest;
import com.academy.fintech.merchant_provider.rest.issuance.CheckDisbursementResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class DisbursementMockServerTest {

    @Autowired
    private DisbursementMockServer disbursementMockServer;

    @Test
    public void disbursementTest() {
        Disbursement payment1 = Disbursement.builder()
                .clientEmail("client1@email.ru")
                .agreementId("agreement1")
                .amount(20000)
                .build();

        String paymentId1 = disbursementMockServer.disburse(payment1).paymentId();
        Assertions.assertNotNull(paymentId1);

        Disbursement payment2 = Disbursement.builder()
                .clientEmail("client2@email.ru")
                .agreementId("agreement2")
                .amount(20000)
                .build();

        String paymentId2 = disbursementMockServer.disburse(payment2).paymentId();
        Assertions.assertNotNull(paymentId2);

        Assertions.assertNotEquals(paymentId1, paymentId2);
    }

    @Test
    public void checkDisbursementExistingPaymentTest() {
        Disbursement payment = Disbursement.builder()
                .clientEmail("client1@email.ru")
                .agreementId("agreement3")
                .amount(20000)
                .build();

        String paymentId = disbursementMockServer.disburse(payment).paymentId();
        ResponseEntity<CheckDisbursementResponse> response = disbursementMockServer.checkDisbursement(CheckDisbursementRequest.builder()
                .paymentId(paymentId)
                .build());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void checkDisbursementNotExistingPaymentTest() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, disbursementMockServer.checkDisbursement(CheckDisbursementRequest.builder()
                .paymentId("payment")
                .build()).getStatusCode());
    }
}
