package com.academy.fintech.merchant_provider.core.service.disbursement;

import com.academy.fintech.merchant_provider.rest.issuance.CheckDisbursementRequest;
import com.academy.fintech.merchant_provider.rest.issuance.CheckDisbursementResponse;
import com.academy.fintech.merchant_provider.rest.issuance.DisbursementResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DisbursementMockServer {

    private static final Map<String, ReadyDisbursement> disbursements = new HashMap<>();
    private static int disbursementIdCounter = 1;
    private static final int countThreads = 7;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(countThreads);

    /**
     * Симулирует выдачу платежа, создавая новый платеж и устанвливая платеж в статус выдан через произвольное время от
     * 0 до 7 дней
     */
    public DisbursementResponse disburse(Disbursement disbursement) {
        final int secondsSevenDay = 604800;
        final Random random = new Random();
        final int randomTimeWait = random.nextInt(secondsSevenDay);

        final String idPayment = "operation-" + disbursementIdCounter++;
        disbursements.put(idPayment, ReadyDisbursement.builder()
                .ready(false)
                .build());

        // Планирование изменения флага выдачи платежа на true
        scheduler.schedule(() -> {
            disbursements.get(idPayment).setIssued(LocalDate.now());
        }, randomTimeWait, TimeUnit.SECONDS);

        return DisbursementResponse.builder()
                .paymentId(idPayment)
                .build();
    }

    /**
     * Проверяет установлен статус платежа в выдан
     */
    public ResponseEntity<CheckDisbursementResponse> checkDisbursement(CheckDisbursementRequest request) {
        if (!disbursements.containsKey(request.paymentId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ReadyDisbursement readyIssuance = disbursements.get(request.paymentId());
        return ResponseEntity.ok(CheckDisbursementResponse.builder()
                .readyDisbursement(readyIssuance.isReady())
                .dateDisbursement(readyIssuance.getDateDisbursement())
                .build());
    }
}
