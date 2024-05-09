package com.academy.fintech.origination.core.service.agreement;

import jakarta.annotation.Nullable;
import lombok.Builder;

/**
 * Класс, который хранит результат создания договора:
 * <ul>
 *     <li>Создан ли договор</li>
 *     <li>id договра, если он создан</li>
 *     <li>Сообщение ошибки, если договор не создан</li>
 * </ul>
 */
@Builder
public record CreationAgreementResult(boolean created,
                                      @Nullable String agreementId,
                                      @Nullable String errorMessage) {
}
