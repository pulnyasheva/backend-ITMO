package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckOverdueService {

    private final int DELAY = 24 * 60 * 60 * 1000;
    private final AgreementService agreementService;
    private final PaymentManagerService paymentManagerService;


    /**
     * Вызывает обработку платежей всех договоров, которые находятся в активном статусе
     */
    @Scheduled(fixedDelay = DELAY)
    @Transactional
    public void checkOverdue() {
        List<Agreement> agreements = agreementService.getActiveAgreement();

        for (Agreement agreement : agreements) {
            try {
                paymentManagerService.processingPayments(agreement);
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }
        }
    }
}
