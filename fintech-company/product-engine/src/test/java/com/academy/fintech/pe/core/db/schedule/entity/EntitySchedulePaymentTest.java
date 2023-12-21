package com.academy.fintech.pe.core.db.schedule.entity;

import com.academy.fintech.pe.core.service.agreement.PaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.EntitySchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_schedule_payment")
@Getter
@Builder
@IdClass(CompositeKeyTest.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EntitySchedulePaymentTest {

    @Id
    @ManyToOne
    @JoinColumn(name = "payment_schedule_id")
    private EntityScheduleTest schedule;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "period_payment")
    private BigDecimal periodPayment;

    @Column(name = "interest_payment")
    private BigDecimal interestPayment;

    @Column(name = "principal_payment")
    private BigDecimal principalPayment;

    @Id
    @Column(name = "period_number")
    private int periodNumber;
}