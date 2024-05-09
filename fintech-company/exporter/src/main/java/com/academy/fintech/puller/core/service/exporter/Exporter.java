package com.academy.fintech.puller.core.service.exporter;

import com.academy.fintech.puller.core.service.enumeration.ExportType;

public interface Exporter {

    void export();

    /**
     * Получает тип объектов для экспорта
     */
    ExportType getType();

}
