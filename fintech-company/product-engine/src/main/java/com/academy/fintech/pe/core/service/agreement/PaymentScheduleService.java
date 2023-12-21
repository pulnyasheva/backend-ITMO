package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.calculation.payment_schedule.Functions;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentScheduleService {

    /**
     * Создает новый платежный график для договора.
     *
     * @return Новый платежный графика.
     */
    @Builder
    public Schedule createNewSchedule(Agreement agreement, int versionService) {
        List<SchedulePayment> schedulePayments = new ArrayList<>();
        Schedule schedule = Schedule.builder()
                .agreementId(agreement.id())
                .version(versionService)
                .schedulePayments(schedulePayments)
                .build();
        createSchedulePayments(agreement.loanTerm(),
                agreement.interest(),
                agreement.principalAmount(),
                agreement.disbursementDate(),
                schedulePayments);
        return schedule;
    }

    /**
     * Создает платежи для платежного графика.
     *
     * @param loanTerm         Срок кредита в месяцах.
     * @param interest         Процентная ставка по кредиту.
     * @param principalAmount  Сумма основного долга.
     * @param disbursementDate Дата выдачи кредита.
     */
    private void createSchedulePayments(int loanTerm,
                                        BigDecimal interest,
                                        BigDecimal principalAmount,
                                        LocalDate disbursementDate,
                                        List<SchedulePayment> schedulePayments) {
        LocalDate currentDate = Functions.calculateNextPaymentDate(disbursementDate);
        for (int i = 1; i <= loanTerm; i++) {
            schedulePayments.add(createSchedulePayment(loanTerm, interest, principalAmount, currentDate, i));
            currentDate = Functions.calculateNextPaymentDate(currentDate);
        }
    }

    /**
     * Создает платеж для платежного графика.
     *
     * @param loanTerm        Срок кредита в месяцах.
     * @param interest        Процентная ставка по кредиту.
     * @param principalAmount Сумма основного долга.
     * @param date            Дата платежа.
     * @param periodNumber    Номер периода платежа.
     */
    private SchedulePayment createSchedulePayment(int loanTerm,
                                                  BigDecimal interest,
                                                  BigDecimal principalAmount,
                                                  LocalDate date,
                                                  int periodNumber) {
        final PaymentStatus paymentStatus = PaymentStatus.FUTURE;
        final BigDecimal countPaymentInYear = new BigDecimal(12);
        final int countRound = 10000;
        BigDecimal rate = interest.divide(countPaymentInYear, countRound, RoundingMode.HALF_UP);
        return SchedulePayment.builder()
                .status(paymentStatus)
                .paymentDate(date)
                .periodPayment(Functions.PMT(rate, loanTerm, principalAmount))
                .interestPayment(Functions.IPMT(rate, periodNumber, loanTerm, principalAmount))
                .principalPayment(Functions.PPMT(rate, periodNumber, loanTerm, principalAmount))
                .periodNumber(periodNumber)
                .build();
    }
}
