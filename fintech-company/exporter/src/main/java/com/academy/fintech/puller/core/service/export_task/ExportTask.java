package com.academy.fintech.puller.core.service.export_task;

import com.academy.fintech.puller.core.service.enumeration.ExportType;
import lombok.Builder;

import java.math.BigInteger;

/**
 * Задача для выгрузки в kafka
 * @param id id задачи
 * @param type тип объекта отправки
 * @param stringKey строковый id объекта, если у объекта строковый id
 * @param numberKey числовой id объекта, если у объекта числовой id
 */
@Builder
public record ExportTask(
        BigInteger id,
        ExportType type,
        String stringKey,
        BigInteger numberKey
) { }
