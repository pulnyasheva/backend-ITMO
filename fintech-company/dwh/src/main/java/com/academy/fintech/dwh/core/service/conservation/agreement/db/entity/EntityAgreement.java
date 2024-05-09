package com.academy.fintech.dwh.core.service.conservation.agreement.db.entity;

import com.academy.fintech.dwh.core.service.conservation.agreement.AgreementStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "agreement")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EntityAgreement {

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

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}


