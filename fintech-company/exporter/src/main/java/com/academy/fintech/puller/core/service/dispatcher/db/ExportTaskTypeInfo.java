package com.academy.fintech.puller.core.service.dispatcher.db;

import com.academy.fintech.puller.core.service.enumeration.ExportType;
import org.springframework.beans.factory.annotation.Value;

/**
 * Интерфейс предостоставляет информацию о количестве задач каждого типа
 */
public interface ExportTaskTypeInfo {

    @Value("#{target.export_type}")
    ExportType getExportType();

    @Value("#{target.count_to_process}")
    Integer getCountToProcess();
}
