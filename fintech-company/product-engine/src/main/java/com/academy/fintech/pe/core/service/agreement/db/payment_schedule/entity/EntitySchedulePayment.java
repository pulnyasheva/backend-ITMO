package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity;

import com.academy.fintech.pe.core.service.agreement.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CompositeKey.class)
@Table(name = "payment_schedule_payment")
public class EntitySchedulePayment {

    @Id
    @ManyToOne
    @JoinColumn(name = "payment_schedule_id")
    private EntitySchedule schedule;

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