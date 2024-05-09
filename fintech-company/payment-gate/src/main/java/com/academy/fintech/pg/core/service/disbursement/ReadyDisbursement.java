package com.academy.fintech.pg.core.service.disbursement;

import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDate;

/**
 * Класс, который хранит выдан ли платеж, и если выдан то когда
 */
@Builder
public record ReadyDisbursement(boolean ready,
                                @Nullable LocalDate dateDisbursement) {
}
