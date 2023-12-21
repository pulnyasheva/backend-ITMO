package com.academy.fintech.scoring.core.service;

import com.academy.fintech.check_scoring.CheckScoringRequest;
import com.academy.fintech.check_scoring.CheckScoringResponse;
import com.academy.fintech.scoring.core.pe.client.PaymentPEClientService;
import com.academy.fintech.scoring.core.pe.client.SchedulePEClientService;
import com.academy.fintech.scoring.core.service.application.Application;
import com.academy.fintech.scoring.core.service.application.ScoringService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ScoringServiceTest {

    @Mock
    SchedulePEClientService mockSchedulePEClientService;
    @Mock
    PaymentPEClientService mockPaymentPEClientService;
    ScoringService scoringService;


    @BeforeEach
    public void createScoringServiceTest() {
        scoringService = new ScoringService(mockSchedulePEClientService, mockPaymentPEClientService);
    }

    @Test
    public void ScoringWithNoOverdueAndCorrectDisbursementTest() {
        Mockito.doReturn(Collections.emptyList()).when(mockPaymentPEClientService)
                .getOverduePayments(Mockito.any(String.class));

        Mockito.doReturn(new BigDecimal("15000")).when(mockSchedulePEClientService)
                .createSchedule(Mockito.any(Application.class));

        CheckScoringRequest request = CheckScoringRequest.newBuilder()
                .setClientId("client")
                .setInterest("7")
                .setClientSalary(50000)
                .setLoanTerm(9)
                .build();

        CheckScoringResponse response = scoringService.scoring(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getScore());
    }

    @Test
    public void ScoringWithNoOverdueAndNoCorrectDisbursementTest() {
        Mockito.doReturn(Collections.emptyList()).when(mockPaymentPEClientService)
                .getOverduePayments(Mockito.any(String.class));

        Mockito.doReturn(new BigDecimal("20000")).when(mockSchedulePEClientService)
                .createSchedule(Mockito.any(Application.class));

        CheckScoringRequest request = CheckScoringRequest.newBuilder()
                .setClientId("client")
                .setInterest("7")
                .setClientSalary(50000)
                .setLoanTerm(9)
                .build();

        CheckScoringResponse response = scoringService.scoring(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getScore());
    }

    @Test
    public void ScoringWithOverdueBigger7AndCorrectDisbursementTest() {
        Mockito.doReturn(List.of(LocalDate.of(2022, 11, 12))).when(mockPaymentPEClientService)
                .getOverduePayments(Mockito.any(String.class));

        Mockito.doReturn(new BigDecimal("15000")).when(mockSchedulePEClientService)
                .createSchedule(Mockito.any(Application.class));

        CheckScoringRequest request = CheckScoringRequest.newBuilder()
                .setClientId("client")
                .setInterest("7")
                .setClientSalary(50000)
                .setLoanTerm(9)
                .build();

        CheckScoringResponse response = scoringService.scoring(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getScore());
    }

    @Test
    public void ScoringWithOverdueSmaller7AndCorrectDisbursementTest() {
        Mockito.doReturn(List.of(LocalDate.now().minusDays(5))).when(mockPaymentPEClientService)
                .getOverduePayments(Mockito.any(String.class));

        Mockito.doReturn(new BigDecimal("15000")).when(mockSchedulePEClientService)
                .createSchedule(Mockito.any(Application.class));

        CheckScoringRequest request = CheckScoringRequest.newBuilder()
                .setClientId("client")
                .setInterest("7")
                .setClientSalary(50000)
                .setLoanTerm(9)
                .build();

        CheckScoringResponse response = scoringService.scoring(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getScore());
    }

    @Test
    public void ScoringWithOverdueBigger7AndNoCorrectDisbursementTest() {
        Mockito.doReturn(List.of(LocalDate.of(2023, 10, 1))).when(mockPaymentPEClientService)
                .getOverduePayments(Mockito.any(String.class));

        Mockito.doReturn(new BigDecimal("20000")).when(mockSchedulePEClientService)
                .createSchedule(Mockito.any(Application.class));

        CheckScoringRequest request = CheckScoringRequest.newBuilder()
                .setClientId("client")
                .setInterest("7")
                .setClientSalary(50000)
                .setLoanTerm(9)
                .build();

        CheckScoringResponse response = scoringService.scoring(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(-1, response.getScore());
    }

    @Test
    public void ScoringWithOverdueSmaller7AndNoCorrectDisbursementTest() {
        Mockito.doReturn(List.of(LocalDate.now().minusDays(5))).when(mockPaymentPEClientService)
                .getOverduePayments(Mockito.any(String.class));

        Mockito.doReturn(new BigDecimal("20000")).when(mockSchedulePEClientService)
                .createSchedule(Mockito.any(Application.class));

        CheckScoringRequest request = CheckScoringRequest.newBuilder()
                .setClientId("client")
                .setInterest("7")
                .setClientSalary(50000)
                .setLoanTerm(9)
                .build();

        CheckScoringResponse response = scoringService.scoring(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getScore());
    }
}
