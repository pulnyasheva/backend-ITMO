package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.disbursement.DisbursementResponse;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Сервис для осуществления выдачи средств по договору.
 */
@Service
@RequiredArgsConstructor
public class AgreementDisbursementService {

    private final int NUMBER_VERSION_SCHEDULE = 1;

    private final AgreementService agreementService;

    private final com.academy.fintech.pe.core.service.agreement.db.payment_schedule.ScheduleService scheduleService;

    private final PaymentScheduleService paymentScheduleService;

    /**
     * Осуществляет выдачу средств по запросу. Указывает в договоре даты выдачи средств. Создает график платежей.
     *
     * @param request запрос на выдачу средств.
     * @return ответ с идентификатором созданного графика платежей.
     */
    @Transactional
    public DisbursementResponse disbursement(DisbursementRequest request) {
        LocalDate disbursementDate = LocalDate.of(request.getDisbursementDate().getYear(),
                request.getDisbursementDate().getMonth(),
                request.getDisbursementDate().getDay());
        String agreementId = request.getAgreementId();

        agreementService.updateStatus(agreementId, AgreementStatus.ACTIVE);
        agreementService.setDisbursementDate(agreementId, disbursementDate);

        // Создание и сохранение графика платежей
        Agreement agreement = agreementService.get(agreementId);
        Schedule schedule = paymentScheduleService.createNewSchedule(agreement, NUMBER_VERSION_SCHEDULE);
        String scheduleId = scheduleService.add(schedule);

        return DisbursementResponse.newBuilder()
                .setScheduleId(scheduleId)
                .build();
    }


}
