package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.overdue_payment.Date;
import com.academy.fintech.overdue_payment.OverduePaymentRequest;
import com.academy.fintech.overdue_payment.OverduePaymentResponse;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.ScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final AgreementService agreementService;

    private final ScheduleService scheduleService;

    /**
     * Получение всех первых дат просрочек по всем договорам у клиента.
     */
    @Transactional
    public OverduePaymentResponse getDateOverduePayments(OverduePaymentRequest request) {
        List<Date> overdueDates = new ArrayList<>();
        List<Agreement> agreements = agreementService.getByClientId(request.getClientId());

        for (Agreement agreement : agreements) {
            List<Schedule> schedules = scheduleService.getByAgreementId(agreement.id());

            // Получение последней версии платежа
            Schedule schedulePastVersion = schedules.get(0);
            int pastVersion = schedulePastVersion.version();
            for (Schedule schedule : schedules) {
                if (pastVersion < schedule.version()) {
                    schedulePastVersion = schedule;
                }
            }

            // Получение первой даты просрочки
            LocalDate overdueDate = null;
            for (SchedulePayment schedulePayment : schedulePastVersion.schedulePayments()) {
                if (schedulePayment.getStatus().equals(PaymentStatus.OVERDUE)
                        && (overdueDate == null || overdueDate.isAfter(schedulePayment.getPaymentDate()))) {
                    overdueDate = schedulePayment.getPaymentDate();
                }
            }

            if (overdueDate != null) {
                overdueDates.add(mapLocalDateToDate(overdueDate));
            }
        }

        return OverduePaymentResponse.newBuilder()
                .addAllOverdueDates(overdueDates)
                .build();
    }

    private Date mapLocalDateToDate(LocalDate localDate) {
        return Date.newBuilder()
                .setDay(localDate.getDayOfMonth())
                .setMonth(localDate.getMonthValue())
                .setYear(localDate.getYear())
                .build();
    }
}
