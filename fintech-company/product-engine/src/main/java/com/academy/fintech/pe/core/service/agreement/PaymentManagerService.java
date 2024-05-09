package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.balance.BalanceService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.ScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentManagerService {

    private final ScheduleService scheduleService;
    private final BalanceService balanceService;
    private final AgreementService agreementService;

    /**
     * Обрабатывает платежи
     * <ul>
     *   <li>Пытается погасить просроченные платежи</li>
     *   <li>Пытается оплатить будущие платежи, дата оплаты которых уже подошла</li>
     *   <li>Начисляет на баланс просрочек клиента сумму платежа, если на балансе клиента не хватает суммы для оплаты
     *   этого платежа</li>
     *  </ul>
     *
     * @param agreement договор, платежи которого будут обрабатываться
     */
    @Transactional
    public void processingPayments(Agreement agreement) throws NoSuchElementException {
        Optional<Balance> clientBalance = balanceService.getTypeBalance(agreement.id(), BalanceType.CLIENT);
        Optional<Balance> overdueBalance = balanceService.getTypeBalance(agreement.id(), BalanceType.OVERDUE);

        // Проверка на отсутвие какого-то баланса
        if (clientBalance.isEmpty() || overdueBalance.isEmpty()) {
            throw new NoSuchElementException("Balance not found with agreementId: " + agreement.id());
        }

        // Получение графика платежей со всеми просроченными и будущими платежами в порядке возрастания даты оплаты
        Schedule schedule =
                scheduleService.getStatusesPaymentsOrdered(agreement.id(),
                        List.of(PaymentStatus.OVERDUE, PaymentStatus.FUTURE));

        processingNotPaidPayments(schedule.schedulePayments(), agreement.id(), clientBalance.get(), overdueBalance.get());

        scheduleService.saveAllSchedulePayments(schedule);
        setAmountBalance(clientBalance.get());
        setAmountBalance(overdueBalance.get());

        checkClosedAgreement(schedule.schedulePayments(), agreement.id(), overdueBalance.get());
    }

    /**
     * Обрабатывает все просроченные и будущие платежи
     * <ul>
     *   <li>Если имеются просроченные платежи, то гасит просроченные платежи, если баланс клиента >= суммы
     *   платежа, и меняет статус платежа на {@link PaymentStatus#PAID}</li>
     *   <li>Оплачивает будущие платежи, если даты оплаты подошла и баланс клиента >= суммы платежа, меняет
     *   статус платежа на {@link PaymentStatus#PAID}</li>
     *   <li>Увеличивает баланс просрочек на сумму платежа, если время платежа подошло, а на балансе клиента
     *   недостаточно средств</li>
     *  </ul>
     *
     * @param schedulePayments просроченные и будущие платежи, отсортированные в порядке возрастания даты оплаты
     * @param agreementId      id договора, по которому обрабатываются платежи
     * @param clientBalance    баланс клиента
     * @param overdueBalance   баланс просрочек
     */
    private void processingNotPaidPayments(List<SchedulePayment> schedulePayments,
                                           String agreementId, Balance clientBalance, Balance overdueBalance) {
        BigDecimal amountClient = clientBalance.getAmount();
        BigDecimal amountOverdue = overdueBalance.getAmount();

        // Проверка что у клиента нет просрочек или сумма баланса равна 0
        if (amountClient.compareTo(BigDecimal.ZERO) == 0
                || amountOverdue.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        int indexFuturePayment = -1;

        for (int i = 0; i < schedulePayments.size(); i++) {
            SchedulePayment schedulePayment = schedulePayments.get(i);

            // Проверка, что платеж будущий, и если это так, то сохранение индекса
            if (schedulePayment.getStatus() == PaymentStatus.FUTURE) {
                indexFuturePayment = i;
                break;
            }

            BigDecimal payment = schedulePayment.getPeriodPayment();

            // Проверка, что можно списать просроченный платеж и списание, если возможно
            if (payment.compareTo(amountClient) <= 0) {
                amountClient = amountClient.subtract(payment);
                amountOverdue = amountOverdue.subtract(payment);
                schedulePayment.setStatus(PaymentStatus.PAID);
            }
        }

        clientBalance.setAmount(amountClient);
        overdueBalance.setAmount(amountOverdue);

        // Обработка будущих платежей, если они имеются
        if (indexFuturePayment != -1) {
            processingFuturePayments(schedulePayments, indexFuturePayment,
                    agreementId, clientBalance, overdueBalance);
        }
    }

    /**
     * Если имеются будущие платежи, то пытается оплатить все, дата которох уже подошла. Оплачивает платеж,
     * только в случае, если баланс клиента >= суммы платежа. Обновляет следующую дату платежа у договора.
     *
     * @param schedulePayments просроченные и будущие платежи, отсортированные в порядке возрастания даты оплаты
     * @param startFuture      индекс, с которого начинаются будущие платежи
     * @param agreementId      id договора, по которому обрабатываются платежи
     * @param clientBalance    баланс клиента
     * @param overdueBalance   баланс просрочек
     */
    @Transactional
    private void processingFuturePayments(List<SchedulePayment> schedulePayments, int startFuture,
                                          String agreementId, Balance clientBalance, Balance overdueBalance) {
        BigDecimal amountClient = clientBalance.getAmount();
        BigDecimal amountOverdue = overdueBalance.getAmount();

        LocalDate currentDate = LocalDate.now();

        for (int i = startFuture; i < schedulePayments.size(); i++) {
            SchedulePayment schedulePayment = schedulePayments.get(i);

            // Проверка, что время платежа еще не наступило и смена даты следующего платежа, если не наступила
            if (schedulePayment.getPaymentDate().isAfter(currentDate)) {
                agreementService.setNextPayment(agreementId, schedulePayment.getPaymentDate());
                break;
            }

            BigDecimal payment = schedulePayment.getPeriodPayment();

            // Проверка, что у клиента нет просрочек и есть сумма на балансе для платежа
            if (amountOverdue.compareTo(BigDecimal.ZERO) == 0
                    && payment.compareTo(amountClient) <= 0) {
                // Выполнение платежа
                amountClient = amountClient.subtract(payment);
                schedulePayment.setStatus(PaymentStatus.PAID);
            } else {
                // Начисление просрочки
                amountOverdue = amountOverdue.add(payment);
                schedulePayment.setStatus(PaymentStatus.OVERDUE);
            }
        }

        clientBalance.setAmount(amountClient);
        overdueBalance.setAmount(amountOverdue);
    }

    /**
     * Завершает договор, если последний платеж оплачен и просрочек больше нет
     *
     * @param schedulePayments платежи, которые отстортирвоаны по возрастанию даты оплтаты
     */
    private void checkClosedAgreement(List<SchedulePayment> schedulePayments,
                                      String agreementId, Balance overdueBalance) {

        if (schedulePayments.get(schedulePayments.size() - 1).getStatus() == PaymentStatus.PAID
                && overdueBalance.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            agreementService.setStatus(agreementId, AgreementStatus.CLOSED);
        }
    }

    private void setAmountBalance(Balance balance) {
        balanceService.setAmount(balance.getAgreementId(), balance.getType(), balance.getAmount());
    }
}
