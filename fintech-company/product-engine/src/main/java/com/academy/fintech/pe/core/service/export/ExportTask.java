package com.academy.fintech.pe.core.service.export;

import lombok.Builder;

import java.math.BigInteger;

/**
 * Задача для выгрузки в kafka
 * @param type тип объекта отправки
 * @param stringKey строковый id объекта, если у объекта строковый id
 * @param numberKey числовой id объекта, если у объекта числовой id
 */
@Builder
public record ExportTask(
        String type,
        String stringKey,
        BigInteger numberKey
) { }
