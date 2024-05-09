package com.academy.fintech.pg.core.service.disbursement;

import com.academy.fintech.pg.core.service.agreement.ActivationAgreementService;
import com.academy.fintech.pg.core.service.agreement.ApplicationActivation;
import com.academy.fintech.pg.core.service.disbursement.db.DisbursementService;
import com.academy.fintech.pg.core.merchant_provider.client.CheckDisbursementClientServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CheckDisbursementService {

    private final CheckDisbursementClientServer checkDisbursementClientServer;
    private final DisbursementService disbursementService;
    private final ActivationAgreementService activationAgreementService;
    private static final int countThreads = 7;
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(countThreads);

    /**
     * Начинает проверку выдачи платежа
     */
    public void checkDisbursement(Disbursement disbursement) {
        int startInterval = 0;
        LocalDateTime startDate = LocalDateTime.now();
        checkWithInterval(disbursement, startInterval, startDate);
    }

    /**
     * Планирует проверку выдачи платежа через определенный интервал.
     * <ul>
     *     <li>Если платеж выдан и не прошло 7 дней с начала проверок, планирует следующую проверку через интервал,
     *     который вычисляется как: (текущий интервал + 1) * 10</li>
     *     <li>Если платеж не выдан и ожидание выдачи прошло (прошло 7 дней с начала проверок), меняет статус платежа
     *     на {@link DisbursementStatus#ERROR}</li>
     *     <li>Если платеж выдан, меняет статус платежа на {@link DisbursementStatus#READY} и запрашивает активацию
     *     договора</li>
     * </ul>
     *
     * @param startDate дата начала проверок выдачи платежа
     */
    private void checkWithInterval(Disbursement disbursement, int checkInterval, LocalDateTime startDate) {
        final int additionInterval = 1;
        final int timeIncreaseExponent = 10;
        executor.schedule(() -> {
            final ReadyDisbursement result = checkDisbursementClientServer.checkIssuance(disbursement);
            final boolean checkWaitPeriod = checkWaitPeriod(startDate);

            if (!result.ready() && checkWaitPeriod) {
                checkWithInterval(disbursement, (checkInterval + additionInterval) * timeIncreaseExponent,
                        startDate);

            } else if (result.ready()) {
                disbursementService.changeStatus(disbursement.getId(), DisbursementStatus.READY);
                activationAgreementService.activate(
                        createApplication(disbursement.getAgreementId(), result.dateDisbursement()));

            } else {
                disbursementService.changeStatus(disbursement.getId(), DisbursementStatus.ERROR);
            }

        }, checkInterval, TimeUnit.SECONDS);
    }

    /**
     * Проверяет, что с даты начала проверок не прошло еще 7 дней
     *
     * @param startDate дата начало проверок
     */
    private boolean checkWaitPeriod(LocalDateTime startDate) {
        return Duration.between(startDate, LocalDateTime.now()).toDays() < 7;
    }

    private ApplicationActivation createApplication(String agreementId, LocalDate disbursementdate) {
        return ApplicationActivation.builder()
                .agreementId(agreementId)
                .disbursementDate(disbursementdate)
                .build();
    }
}
