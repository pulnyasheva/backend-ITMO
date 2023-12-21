package com.academy.fintech.pe.core.service.agreement.db.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
public class EntityProduct {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "min_loan_term")
    private int minLoanTerm;

    @Column(name = "max_loan_term")
    private int maxLoanTerm;

    @Column(name = "min_principal_amount")
    private BigDecimal minPrincipalAmount;

    @Column(name = "max_principal_amount")
    private BigDecimal maxPrincipalAmount;

    @Column(name = "min_interest")
    private BigDecimal minInterest;

    @Column(name = "max_interest")
    private BigDecimal maxInterest;

    @Column(name = "min_origination_amount")
    private BigDecimal minOriginationAmount;

    @Column(name = "max_origination_amount")
    private BigDecimal maxOriginationAmount;
}
