package com.academy.fintech.merchant_provider.core.service.disbursement;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс, который хранит выдан ли платеж, и если выдан то когда
 */
@Builder
@Getter
@Setter
public class ReadyDisbursement {

    private boolean ready;

    @Nullable
    private LocalDate dateDisbursement;

    public void setIssued(LocalDate dateDisbursement) {
        this.ready = true;
        this.dateDisbursement = dateDisbursement;
    }
}
