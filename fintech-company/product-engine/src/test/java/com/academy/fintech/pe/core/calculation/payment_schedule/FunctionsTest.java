package com.academy.fintech.pe.core.calculation.payment_schedule;

import com.academy.fintech.pe.core.calculation.payment_schedule.Functions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionsTest {

    private final BigDecimal COUNT_PAYMENT_IN_YEAR = new BigDecimal(12);

    private final BigDecimal INTEREST = new BigDecimal(8);
    private final BigDecimal RATE = INTEREST.divide(COUNT_PAYMENT_IN_YEAR, 1000, RoundingMode.HALF_UP);
    private final int COUNT_PERIOD = 12;
    private final BigDecimal PV = new BigDecimal(500000);

    private final RoundingMode ROUND = RoundingMode.DOWN;

    @Test
    public void testPMT() {
        BigDecimal expectedPMT = new BigDecimal("43494.21454");

        BigDecimal result = Functions.PMT(RATE, COUNT_PERIOD, PV);
        assertEquals(expectedPMT, result.setScale(5, ROUND));
    }

    @Test
    public void testPPMT() {
        BigDecimal[] expectedPPMT = {new BigDecimal("40160.88120"), new BigDecimal("40428.62041"),
                new BigDecimal("40698.14455"), new BigDecimal("40969.46551"),
                new BigDecimal("41242.59528"), new BigDecimal("41517.54592"),
                new BigDecimal("41794.32956"), new BigDecimal("42072.95842"),
                new BigDecimal("42353.44481"), new BigDecimal("42635.80111"),
                new BigDecimal("42920.03978"), new BigDecimal("43206.17338")};

        for (int period = 1; period <= COUNT_PERIOD; period++) {
            BigDecimal result = Functions.PPMT(RATE, period, COUNT_PERIOD, PV);
            assertEquals(expectedPPMT[period - 1], result.setScale(5, ROUND));
        }
    }

    @Test
    public void testIPMT() {
        BigDecimal[] expectedIPMT = {new BigDecimal("3333.33333"), new BigDecimal("3065.59412"),
                new BigDecimal("2796.06998"), new BigDecimal("2524.74902"),
                new BigDecimal("2251.61925"), new BigDecimal("1976.66862"),
                new BigDecimal("1699.88498"), new BigDecimal("1421.25611"),
                new BigDecimal("1140.76972"), new BigDecimal("858.41342"),
                new BigDecimal("574.17475"), new BigDecimal("288.04115")};

        for (int period = 1; period <= COUNT_PERIOD; period++) {
            BigDecimal result = Functions.IPMT(RATE, period, COUNT_PERIOD, PV);
            assertEquals(expectedIPMT[period - 1], result.setScale(5, ROUND));
        }
    }


    @Test
    public void testCalculateNextPaymentDate() {
        LocalDate currentDate = LocalDate.of(2022, 3, 15);
        LocalDate expectedNextPaymentDate = LocalDate.of(2022, 4, 15);

        LocalDate result = Functions.calculateNextPaymentDate(currentDate);
        assertEquals(expectedNextPaymentDate, result);
    }

    @Test
    public void testCalculateNextPaymentBigDate() {
        LocalDate currentDate = LocalDate.of(2022, 3, 31);
        LocalDate expectedNextPaymentDate = LocalDate.of(2022, 4, 30);

        LocalDate result = Functions.calculateNextPaymentDate(currentDate);
        assertEquals(expectedNextPaymentDate, result);
    }

    @Test
    public void testCalculateNextPaymentEndYearDate() {
        LocalDate currentDate = LocalDate.of(2022, 12, 15);
        LocalDate expectedNextPaymentDate = LocalDate.of(2023, 1, 15);

        LocalDate result = Functions.calculateNextPaymentDate(currentDate);
        assertEquals(expectedNextPaymentDate, result);
    }
}
