package com.academy.fintech.origination.core.service.application;

import lombok.Builder;

/**
 * Класс, который хранит id заявки и является ли она дубликатом
 */
@Builder
public record ApplicationDuplicate(String applicationId,
                                   boolean isDuplicated) {
}
