package com.academy.fintech.pe.core.service.agreement.db.agreement.entity;

import com.academy.fintech.pe.core.service.agreement.AgreementStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "agreement")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EntityAgreement {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
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

