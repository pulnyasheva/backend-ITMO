package com.academy.fintech.pe.core.db.agreement.entity;

import com.academy.fintech.pe.core.service.agreement.AgreementStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "agreement")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EntityAgreementTest {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "interest")
    private BigDecimal interest;

    @Column(name = "term")
    private int loanTerm;

    @Column(name = "principal_amount")
    private BigDecimal principalAmount;

    @Column(name = "origination_amount")
    private BigDecimal originationAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AgreementStatus status;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;
}

