package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.create.CreateRequest;
import com.academy.fintech.create.CreateResponse;
import com.academy.fintech.create.CreateServiceGrpc;
import com.academy.fintech.create_schedule.CreateScheduleRequest;
import com.academy.fintech.create_schedule.CreateScheduleResponse;
import com.academy.fintech.create_schedule.CreateScheduleServiceGrpc;
import com.academy.fintech.disbursement.Date;
import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.disbursement.DisbursementResponse;
import com.academy.fintech.disbursement.DisbursementServiceGrpc;
import com.academy.fintech.overdue_payment.OverduePaymentRequest;
import com.academy.fintech.overdue_payment.OverduePaymentResponse;
import com.academy.fintech.overdue_payment.OverduePaymentServiceGrpc;
import com.academy.fintech.pe.Container;
import com.academy.fintech.pe.core.db.agreement.AgreementServiceTest;
import com.academy.fintech.pe.core.db.agreement.entity.EntityAgreementTest;
import com.academy.fintech.pe.core.db.balance.BalanceServiceTest;
import com.academy.fintech.pe.core.db.balance.entity.CompositeKeyTest;
import com.academy.fintech.pe.core.db.balance.entity.EntityBalanceTest;
import com.academy.fintech.pe.core.db.schedule.ScheduleServiceTest;
import com.academy.fintech.pe.core.db.schedule.entity.EntitySchedulePaymentTest;
import com.academy.fintech.pe.core.db.schedule.entity.EntityScheduleTest;
import com.academy.fintech.pe.core.service.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.BalanceType;
import com.academy.fintech.pe.core.service.agreement.PaymentStatus;
import com.academy.fintech.processing.ProcessingRequest;
import com.academy.fintech.processing.ProcessingResponse;
import com.academy.fintech.processing.ProcessingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.academy.fintech.pe.Container.applicationContainer;

@ExtendWith(Container.class)
@SpringBootTest
public final class GrpcTest {

    private static CreateServiceGrpc.CreateServiceBlockingStub grpcServiceCreate;
    private static DisbursementServiceGrpc.DisbursementServiceBlockingStub grpcServiceDisbursement;
    private static CreateScheduleServiceGrpc.CreateScheduleServiceBlockingStub grpcServiceCreateSchedule;
    private static OverduePaymentServiceGrpc.OverduePaymentServiceBlockingStub grpcServiceOverduePayment;
    private static ProcessingServiceGrpc.ProcessingServiceBlockingStub grpcServiceProcessing;
    @Autowired
    private AgreementServiceTest agreementService;
    @Autowired
    private ScheduleServiceTest scheduleService;
    @Autowired
    private BalanceServiceTest balanceService;

    @BeforeAll
    public static void AgreementManagementTest() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(applicationContainer.getHost(),
                applicationContainer.getGrpcPort()).usePlaintext().build();
        grpcServiceCreate = CreateServiceGrpc.newBlockingStub(channel);
        grpcServiceDisbursement = DisbursementServiceGrpc.newBlockingStub(channel);
        grpcServiceCreateSchedule = CreateScheduleServiceGrpc.newBlockingStub(channel);
        grpcServiceOverduePayment = OverduePaymentServiceGrpc.newBlockingStub(channel);
        grpcServiceProcessing = ProcessingServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void createAgreementCorrectTest() {
        CreateRequest request = CreateRequest.newBuilder()
                .setClientId("client1")
                .setLoanTerm(7)
                .setDisbursementAmount("55000")
                .setOriginationAmount("2500")
                .setInterest("9")
                .setProductCode("CL1.0")
                .build();
        CreateResponse response = grpcServiceCreate.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.hasAgreementId());
        Assertions.assertNotNull(response.getAgreementId());

        EntityAgreementTest expectedAgreement = EntityAgreementTest.builder()
                .id(response.getAgreementId())
                .productCode("CL1.0")
                .clientId("client1")
                .loanTerm(7)
                .interest(new BigDecimal(9))
                .principalAmount(new BigDecimal("57500"))
                .originationAmount(new BigDecimal("2500"))
                .status(AgreementStatus.NEW)
                .build();

        Optional<EntityAgreementTest> agreement = agreementService.get(response.getAgreementId());
        Assertions.assertTrue(agreement.isPresent());
        Assertions.assertEquals(agreement.get(), expectedAgreement);
    }

    @Test
    public void createAgreementNoCorrectProductTest() {
        CreateRequest request = CreateRequest.newBuilder()
                .setClientId("client1")
                .setLoanTerm(7)
                .setDisbursementAmount("55000")
                .setOriginationAmount("2500")
                .setInterest("9")
                .setProductCode("CL1.1")
                .build();
        CreateResponse response = grpcServiceCreate.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.hasErrorMessage());
        Assertions.assertEquals(response.getErrorMessage(), "Product CL1.1 does not exist");
    }

    @Test
    public void createAgreementNoCorrectTermTest() {
        CreateRequest request = CreateRequest.newBuilder()
                .setClientId("client1")
                .setLoanTerm(43)
                .setDisbursementAmount("55000")
                .setOriginationAmount("2500")
                .setInterest("9")
                .setProductCode("CL1.0")
                .build();
        CreateResponse response = grpcServiceCreate.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.hasErrorMessage());
        Assertions.assertEquals(response.getErrorMessage(),
                "Invalid loan term, acceptable values are from 3 to 24");
    }

    @Test
    public void createAgreementNoCorrectDisbursementTest() {
        CreateRequest request = CreateRequest.newBuilder()
                .setClientId("client1")
                .setLoanTerm(7)
                .setDisbursementAmount("55000000000")
                .setOriginationAmount("2500")
                .setInterest("9")
                .setProductCode("CL1.0")
                .build();
        CreateResponse response = grpcServiceCreate.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.hasErrorMessage());
        Assertions.assertEquals(response.getErrorMessage(),
                "Invalid principal amount, acceptable values are from 50000 to 500000");
    }

    @Test
    public void createAgreementNoCorrectInterestTest() {
        CreateRequest request = CreateRequest.newBuilder()
                .setClientId("client1")
                .setLoanTerm(7)
                .setDisbursementAmount("55000")
                .setOriginationAmount("2500")
                .setInterest("45")
                .setProductCode("CL1.0")
                .build();
        CreateResponse response = grpcServiceCreate.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.hasErrorMessage());
        Assertions.assertEquals(response.getErrorMessage(),
                "Invalid interest, acceptable values are from 8 to 15");
    }

    @Test
    public void createAgreementNoCorrectOriginationTest() {
        CreateRequest request = CreateRequest.newBuilder()
                .setClientId("client1")
                .setLoanTerm(7)
                .setDisbursementAmount("55000")
                .setOriginationAmount("2500000")
                .setInterest("9")
                .setProductCode("CL1.0")
                .build();
        CreateResponse response = grpcServiceCreate.create(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.hasErrorMessage());
        Assertions.assertEquals(response.getErrorMessage(),
                "Invalid origination amount, acceptable values are from 2000 to 10000");
    }

    @Test
    public void disbursementAgreementTest() {
        CreateRequest createRequest = CreateRequest.newBuilder()
                .setClientId("client2")
                .setLoanTerm(7)
                .setDisbursementAmount("55000")
                .setOriginationAmount("2500")
                .setInterest("9")
                .setProductCode("CL1.0")
                .build();
        CreateResponse createResponse = grpcServiceCreate.create(createRequest);
        DisbursementRequest disbursementRequest = DisbursementRequest.newBuilder()
                .setAgreementId(createResponse.getAgreementId())
                .setDisbursementDate(Date.newBuilder()
                        .setYear(2023)
                        .setMonth(9)
                        .setDay(18)
                        .build())
                .build();
        String agreementId = createResponse.getAgreementId();
        DisbursementResponse disbursementResponse = grpcServiceDisbursement.disbursement(disbursementRequest);
        Assertions.assertNotNull(disbursementResponse);

        EntityScheduleTest expectedSchedule = EntityScheduleTest.builder()
                .id(disbursementResponse.getScheduleId())
                .agreementId(agreementId)
                .version(1)
                .build();

        Optional<EntityScheduleTest> schedule = scheduleService.get(disbursementResponse.getScheduleId());
        Assertions.assertTrue(schedule.isPresent());
        Assertions.assertEquals(schedule.get(), expectedSchedule);

        List<EntitySchedulePaymentTest> schedulePaymentTestList = schedule.get().getSchedulePayments();
        Assertions.assertEquals(schedulePaymentTestList.size(), 7);
    }

    @Test
    public void scoringCreateScheduleTest() {
        CreateScheduleRequest request = CreateScheduleRequest.newBuilder()
                .setLoanTerm(7)
                .setDisbursementAmount("57000")
                .setInterest("9")
                .build();

        CreateScheduleResponse response = grpcServiceCreateSchedule.create(request);
        Assertions.assertNotNull(response);
    }

    @Test
    public void getOverduePaymentsTest() {
        EntityAgreementTest agreement1 = EntityAgreementTest.builder()
                .clientId("clientTest")
                .id("agreement1")
                .build();
        EntityScheduleTest schedule1 = EntityScheduleTest.builder()
                .id("schedule1")
                .agreementId("agreement1")
                .version(1)
                .build();
        EntitySchedulePaymentTest schedulePayment1_1 = EntitySchedulePaymentTest.builder()
                .schedule(schedule1)
                .periodNumber(1)
                .status(PaymentStatus.OVERDUE)
                .paymentDate(LocalDate.of(2022, 12, 1))
                .build();
        schedule1.setSchedulePayments(Collections.singletonList(schedulePayment1_1));

        EntityAgreementTest agreement2 = EntityAgreementTest.builder()
                .clientId("clientTest")
                .id("agreement2")
                .build();
        EntityScheduleTest schedule2 = EntityScheduleTest.builder()
                .id("schedule2")
                .agreementId("agreement2")
                .version(1)
                .build();
        EntitySchedulePaymentTest schedulePayment2_1 = EntitySchedulePaymentTest.builder()
                .schedule(schedule2)
                .periodNumber(1)
                .status(PaymentStatus.OVERDUE)
                .paymentDate(LocalDate.of(2023, 11, 1))
                .build();
        EntitySchedulePaymentTest schedulePayment2_2 = EntitySchedulePaymentTest.builder()
                .schedule(schedule2)
                .periodNumber(2)
                .status(PaymentStatus.OVERDUE)
                .paymentDate(LocalDate.of(2023, 12, 1))
                .build();
        schedule2.setSchedulePayments(Arrays.asList(schedulePayment2_1, schedulePayment2_2));

        EntityAgreementTest agreement3 = EntityAgreementTest.builder()
                .clientId("clientTest")
                .id("agreement3")
                .build();
        EntityScheduleTest schedule3 = EntityScheduleTest.builder()
                .id("schedule3")
                .agreementId("agreement3")
                .version(1)
                .build();
        EntitySchedulePaymentTest schedulePayment3_1 = EntitySchedulePaymentTest.builder()
                .schedule(schedule3)
                .periodNumber(1)
                .status(PaymentStatus.FUTURE)
                .paymentDate(LocalDate.of(2022, 11, 1))
                .build();
        schedule3.setSchedulePayments(Collections.singletonList(schedulePayment3_1));

        agreementService.add(agreement1);
        agreementService.add(agreement2);
        agreementService.add(agreement3);
        scheduleService.add(schedule1);
        scheduleService.add(schedule2);
        scheduleService.add(schedule3);

        OverduePaymentRequest request = OverduePaymentRequest.newBuilder()
                .setClientId("clientTest")
                .build();

        OverduePaymentResponse response = grpcServiceOverduePayment.getOverduePayments(request);

        List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(LocalDate.of(2022, 12, 1),
                LocalDate.of(2023, 11, 1)));

        List<LocalDate> dates = new ArrayList<>();
        for (com.academy.fintech.overdue_payment.Date date : response.getOverdueDatesList()) {
            dates.add(LocalDate.of(date.getYear(), date.getMonth(), date.getDay()));
        }

        Assertions.assertNotNull(response);
        Assertions.assertTrue(equalsLists(expectedDates, dates));
    }

    @Test
    public void processingTest() {
        String agreementId = "agreement";
        EntityAgreementTest agreement = EntityAgreementTest.builder()
                .clientId("clientTest")
                .id(agreementId)
                .build();

        agreementService.add(agreement);

        EntityBalanceTest balance = EntityBalanceTest.builder()
                .agreementId(agreementId)
                .amount(new BigDecimal(0))
                .type(BalanceType.CLIENT)
                .build();

        balanceService.add(balance);

        ProcessingRequest request = ProcessingRequest.newBuilder()
                .setAgreementId(agreementId)
                .setAmountPayment(20000)
                .build();

        ProcessingResponse response = grpcServiceProcessing.processing(request);

        Assertions.assertFalse(response.hasErrorMessage());

        Optional<EntityBalanceTest> modifiedBalance = balanceService.get(CompositeKeyTest.builder()
                .agreementId(agreementId)
                .type(BalanceType.CLIENT)
                .build());

        Assertions.assertTrue(modifiedBalance.isPresent());
        Assertions.assertEquals(new BigDecimal(20000), modifiedBalance.get().getAmount());
    }

    private boolean equalsLists(List<LocalDate> list1, List<LocalDate> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        List<LocalDate> tempList1 = new ArrayList<>(list1);
        List<LocalDate> tempList2 = new ArrayList<>(list2);

        tempList1.removeAll(list2);
        tempList2.removeAll(list1);

        return tempList1.isEmpty() && tempList2.isEmpty();
    }
}
