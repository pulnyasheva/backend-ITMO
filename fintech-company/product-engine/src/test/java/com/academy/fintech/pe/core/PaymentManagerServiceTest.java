package com.academy.fintech.pe.core;

import com.academy.fintech.pe.Container;
import com.academy.fintech.pe.core.db.agreement.AgreementServiceTest;
import com.academy.fintech.pe.core.db.agreement.entity.EntityAgreementTest;
import com.academy.fintech.pe.core.db.balance.BalanceServiceTest;
import com.academy.fintech.pe.core.db.balance.entity.CompositeKeyTest;
import com.academy.fintech.pe.core.db.balance.entity.EntityBalanceTest;
import com.academy.fintech.pe.core.db.schedule.ScheduleServiceTest;
import com.academy.fintech.pe.core.db.schedule.entity.EntitySchedulePaymentTest;
import com.academy.fintech.pe.core.db.schedule.entity.EntityScheduleTest;
import com.academy.fintech.pe.core.service.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.BalanceType;
import com.academy.fintech.pe.core.service.agreement.PaymentManagerService;
import com.academy.fintech.pe.core.service.agreement.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(Container.class)
@SpringBootTest
public class PaymentManagerServiceTest {

    @Autowired
    private PaymentManagerService paymentManagerService;
    @Autowired
    private AgreementServiceTest agreementService;
    @Autowired
    private ScheduleServiceTest scheduleService;
    @Autowired
    private BalanceServiceTest balanceService;

    @Test
    public void processingOverduePaymentsTest() {
        String agreementId = "agreement1";
        Agreement agreement = createAgreement(agreementId);

        LocalDate testDate = LocalDate.now().plusDays(5);
        createSchedule(agreementId, testDate, PaymentStatus.OVERDUE);

        createBalances(agreementId, 25000, 40000);

        paymentManagerService.processingPayments(agreement);

        checkAgreement(agreementId, AgreementStatus.ACTIVE);
        checkBalances(agreementId, 20000);
        checkSchedule(PaymentStatus.OVERDUE, PaymentStatus.FUTURE);
    }

    @Test
    public void processingFutureToOverduePaymentsTest() {
        String agreementId = "agreement2";
        Agreement agreement = createAgreement(agreementId);

        LocalDate testDate = LocalDate.now().minusDays(5);
        createSchedule(agreementId, testDate, PaymentStatus.OVERDUE);

        createBalances(agreementId, 45000, 40000);

        paymentManagerService.processingPayments(agreement);

        checkAgreement(agreementId, AgreementStatus.ACTIVE);
        checkBalances(agreementId, 20000);
        checkSchedule(PaymentStatus.PAID, PaymentStatus.OVERDUE);
    }

    @Test
    public void processingFutureToPaidPaymentsTest() {
        String agreementId = "agreement3";
        Agreement agreement = createAgreement(agreementId);

        LocalDate testDate = LocalDate.now().minusDays(5);
        createSchedule(agreementId, testDate, PaymentStatus.PAID);

        createBalances(agreementId, 45000, 20000);

        paymentManagerService.processingPayments(agreement);

        checkAgreement(agreementId, AgreementStatus.CLOSED);
        checkBalances(agreementId, 0);
        checkSchedule(PaymentStatus.PAID, PaymentStatus.PAID);
    }

    private Agreement createAgreement(String agreementId) {
        EntityAgreementTest agreement = EntityAgreementTest.builder()
                .id(agreementId)
                .status(AgreementStatus.ACTIVE)
                .build();
        agreementService.add(agreement);
        return mapEntityTestToAgreement(agreement);
    }

    private void createSchedule(String agreementId, LocalDate testDate,
                                PaymentStatus status) {
        EntityScheduleTest schedule = EntityScheduleTest.builder()
                .id("schedule")
                .agreementId(agreementId)
                .version(1)
                .build();

        EntitySchedulePaymentTest schedulePayment1 =
                createSchedulePayment(schedule,1,
                        testDate.minusMonths(3), PaymentStatus.PAID);

        EntitySchedulePaymentTest schedulePayment2 =
                createSchedulePayment(schedule,2,
                        testDate.minusMonths(2), status);

        EntitySchedulePaymentTest schedulePayment3 =
                createSchedulePayment(schedule,3,
                        testDate.minusMonths(1), PaymentStatus.OVERDUE);
        EntitySchedulePaymentTest schedulePayment4 =
                createSchedulePayment(schedule,4,
                        testDate, PaymentStatus.FUTURE);

        schedule.setSchedulePayments(
                List.of(schedulePayment1, schedulePayment2, schedulePayment3, schedulePayment4));
        scheduleService.add(schedule);
    }

    private EntitySchedulePaymentTest createSchedulePayment(EntityScheduleTest schedule, int periodNumber,
                                                            LocalDate testDate, PaymentStatus status) {
        return EntitySchedulePaymentTest.builder()
                .schedule(schedule)
                .periodPayment(new BigDecimal(20000))
                .periodNumber(periodNumber)
                .paymentDate(testDate)
                .status(status)
                .build();
    }

    private void createBalances(String agreementId, int clientAmount, int overdueAmount) {
        createBalance(agreementId, BalanceType.CLIENT, clientAmount);
        createBalance(agreementId, BalanceType.OVERDUE, overdueAmount);
    }

    private void createBalance(String agreementId, BalanceType type, int amount) {
        EntityBalanceTest balance = EntityBalanceTest.builder()
                .agreementId(agreementId)
                .type(type)
                .amount(new BigDecimal(amount))
                .build();
        balanceService.add(balance);
    }

    private void checkAgreement(String agreementId, AgreementStatus status) {
        Optional<EntityAgreementTest> agreementResult = agreementService.get(agreementId);
        Assertions.assertTrue(agreementResult.isPresent());
        Assertions.assertEquals(status, agreementResult.get().getStatus());
    }

    private void checkBalances(String agreementId, int overdueAmount) {
        checkBalance(agreementId, BalanceType.CLIENT, 5000);
        checkBalance(agreementId, BalanceType.OVERDUE, overdueAmount);
    }

    private void checkBalance(String agreementId, BalanceType type, int amount) {
        Optional<EntityBalanceTest> clientBalanceResult = balanceService.get(CompositeKeyTest.builder()
                .agreementId(agreementId)
                .type(type)
                .build());
        Assertions.assertTrue(clientBalanceResult.isPresent());
        Assertions.assertEquals(new BigDecimal(amount), clientBalanceResult.get().getAmount());
    }

    private void checkSchedule(PaymentStatus status2, PaymentStatus status3) {
        Optional<EntityScheduleTest> scheduleResult = scheduleService.get("schedule");
        Assertions.assertTrue(scheduleResult.isPresent());

        for (EntitySchedulePaymentTest schedulePaymentResult : scheduleResult.get().getSchedulePayments()) {
            if (schedulePaymentResult.getPeriodNumber() == 1) {
                Assertions.assertEquals(PaymentStatus.PAID, schedulePaymentResult.getStatus());
            }
            if (schedulePaymentResult.getPeriodNumber() == 2) {
                Assertions.assertEquals(PaymentStatus.PAID, schedulePaymentResult.getStatus());
            }
            if (schedulePaymentResult.getPeriodNumber() == 3) {
                Assertions.assertEquals(status2, schedulePaymentResult.getStatus());
            }
            if (schedulePaymentResult.getPeriodNumber() == 4) {
                Assertions.assertEquals(status3, schedulePaymentResult.getStatus());
            }
        }
    }

    private Agreement mapEntityTestToAgreement(EntityAgreementTest agreementTest) {
        return Agreement.builder()
                .id(agreementTest.getId())
                .status(agreementTest.getStatus())
                .nextPaymentDate(agreementTest.getNextPaymentDate())
                .build();
    }
}
