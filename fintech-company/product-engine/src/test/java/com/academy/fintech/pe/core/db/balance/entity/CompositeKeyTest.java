package com.academy.fintech.pe.core.db.balance.entity;

import com.academy.fintech.pe.core.service.agreement.BalanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompositeKeyTest implements Serializable {

    private String agreementId;
    private BalanceType type;
}
