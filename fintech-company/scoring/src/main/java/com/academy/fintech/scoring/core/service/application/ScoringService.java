package com.academy.fintech.scoring.core.service.application;

import com.academy.fintech.check_scoring.CheckScoringRequest;
import com.academy.fintech.check_scoring.CheckScoringResponse;
import com.academy.fintech.scoring.core.pe.client.PaymentPEClientService;
import com.academy.fintech.scoring.core.pe.client.SchedulePEClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoringService {

    private static final int COUNT_ROUND = 100;
    private static final RoundingMode ROUND = RoundingMode.HALF_UP;
    private final SchedulePEClientService schedulePEClientService;
    private final PaymentPEClientService paymentPEClientService;

    /**
     * Рассчитывается кредитный рейтинг клиента на основе его кредитной истории и суммы регулярного платежа относительно
     * его зарплаты.
     */
    public CheckScoringResponse scoring(CheckScoringRequest request) {
        int score = 0;

        Application application = mapRequestToApplication(request);
        BigDecimal periodPayment = schedulePEClientService.createSchedule(application);

        if (isCorrectDisbursementAmount(application.clientSalary(), periodPayment)) score += 1;

        score += checkOverduePayments(paymentPEClientService.getOverduePayments(application.clientId()));

        return CheckScoringResponse.newBuilder()
                .setScore(score)
                .build();
    }

    /**
     * Проверка, что сумма регулярного платежа не превышает трети зарплаты клиента.
     *
     * @param salary        зарплата клиента
     * @param periodPayment сумма регулярного платежа
     */
    private boolean isCorrectDisbursementAmount(int salary, BigDecimal periodPayment) {
        return periodPayment.compareTo(new BigDecimal(salary).divide(new BigDecimal(3), COUNT_ROUND, ROUND)) <= 0;
    }

    /**
     * Рассчитывается кредитный рейтинг клиента на основе его кредитной истории.
     * <ul>
     *     <li> Если у клиента есть просрочка более 7 дней, он получает -1 балл </li>
     *     <li> Если просрочка составляет до 7 дней, клиент получает 0 баллов</li>
     *     <li> Если у клиента есть кредит без просрочки или вообще нет кредита, клиент получает 1 балл</li>
     * </ul>
     *
     * @param dates даты просрочек клиента
     * @return целое число, представляющее количество баллов, заработанных клиентом на основе его кредитной истории
     */
    private int checkOverduePayments(List<LocalDate> dates) {
        final int maxDateOverdue = 7;
        if (dates.isEmpty()) return 1;
        LocalDate nowDate = LocalDate.now();
        LocalDate overdueDate = dates.get(0);
        for (LocalDate date : dates) {
            if (date.isBefore(overdueDate)) {
                overdueDate = date;
            }
        }
        long daysBetween = ChronoUnit.DAYS.between(overdueDate, nowDate);
        if (daysBetween > maxDateOverdue) return -1;
        return 0;
    }

    private Application mapRequestToApplication(CheckScoringRequest request) {
        return Application.builder()
                .clientId(request.getClientId())
                .clientSalary(request.getClientSalary())
                .loanTerm(request.getLoanTerm())
                .disbursementAmount(request.getDisbursementAmount())
                .interest(new BigDecimal(request.getInterest()))
                .build();
    }
}
