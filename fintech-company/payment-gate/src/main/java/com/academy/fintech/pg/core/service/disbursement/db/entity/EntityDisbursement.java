package com.academy.fintech.pg.core.service.disbursement.db.entity;

import com.academy.fintech.pg.core.service.disbursement.DisbursementStatus;
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

@Entity
@Table(name = "issue_payment")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityDisbursement {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "agreement_id")
    private String agreementId;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DisbursementStatus status;
}
