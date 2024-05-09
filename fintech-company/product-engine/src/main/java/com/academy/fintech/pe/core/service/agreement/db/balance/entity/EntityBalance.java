package com.academy.fintech.pe.core.service.agreement.db.balance.entity;

import com.academy.fintech.pe.core.service.agreement.BalanceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CompositeKey.class)
@Table(name = "balance")
public class EntityBalance {

    @Id
    @Column(name = "agreement_id")
    private String agreementId;

    @Id
    @Column(name = "balance_type")
    @Enumerated(EnumType.STRING)
    private BalanceType type;

    @Column(name = "amount")
    private BigDecimal amount;
}
