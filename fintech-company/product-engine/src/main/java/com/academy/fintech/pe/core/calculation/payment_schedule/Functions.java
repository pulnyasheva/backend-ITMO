package com.academy.fintech.pe.core.calculation.payment_schedule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Этот класс содержит различные финансовые функции для расчета графиков платежей.
 */
public class Functions {

    /**
     * Константа для преобразования процентов в дробь.
     */
    public static final BigDecimal PERCENT_TO_FRACTION = new BigDecimal(100);

    /**
     * Количество десятичных знаков для округления.
     */
    private static final int COUNT_ROUND = 10000;
    private static final RoundingMode ROUND = RoundingMode.HALF_UP;

    /**
     * Рассчитывает сумму платежа.
     *
     * @param rate        процентная ставка
     * @param countPeriod количество периодов
     * @param pv          текущая стоимость
     * @return сумма платежа
     */
    public static BigDecimal PMT(BigDecimal rate, int countPeriod, BigDecimal pv) {
        rate = rate.divide(PERCENT_TO_FRACTION, COUNT_ROUND, ROUND);
        return (rate.multiply(pv))
                .divide(BigDecimal.ONE
                                .subtract(BigDecimal.ONE
                                        .divide(BigDecimal.ONE
                                                .add(rate)
                                                .pow(countPeriod), COUNT_ROUND, ROUND)),
                        COUNT_ROUND,
                        ROUND);
    }

    /**
     * Рассчитывает основной платеж для определенного периода.
     *
     * @param rate        процентная ставка
     * @param period      период, для которого рассчитывается основной платеж
     * @param countPeriod общее количество периодов
     * @param pv          текущая стоимость
     * @return основной платеж для указанного периода
     */
    public static BigDecimal PPMT(BigDecimal rate, int period, int countPeriod, BigDecimal pv) {
        BigDecimal pmt = PMT(rate, countPeriod, pv);
        return pmt.subtract(IPMT(rate, period, countPeriod, pv));
    }

    /**
     * Рассчитывает процентный платеж для определенного периода.
     *
     * @param rate        процентная ставка
     * @param period      период, для которого рассчитывается процентный платеж
     * @param countPeriod общее количество периодов
     * @param pv          текущая стоимость
     * @return процентный платеж для указанного периода
     */
    public static BigDecimal IPMT(BigDecimal rate, int period, int countPeriod, BigDecimal pv) {
        BigDecimal pmt = PMT(rate, countPeriod, pv);
        rate = rate.divide(PERCENT_TO_FRACTION, COUNT_ROUND, ROUND);
        BigDecimal ipmt = (pv.multiply(rate))
                .multiply(BigDecimal.ONE
                        .add(rate)
                        .pow(period - 1));
        ipmt = ipmt.divide(BigDecimal.ONE
                        .add(rate)
                        .pow(countPeriod)
                        .subtract(BigDecimal.ONE),
                COUNT_ROUND,
                ROUND);
        return pmt.subtract(ipmt);
    }

    /**
     * Рассчитывает следующую дату платежа на основе текущей даты\.
     *
     * @param currentDate текущая дата
     * @return следующая дата платежа
     */
    public static LocalDate calculateNextPaymentDate(LocalDate currentDate) {

        LocalDate nextMonth = currentDate.plusMonths(1);

        return currentDate.getDayOfMonth() <= nextMonth.lengthOfMonth() ?
                LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), currentDate.getDayOfMonth()) :
                nextMonth.with(TemporalAdjusters.lastDayOfMonth());
    }
}
