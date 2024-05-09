package com.academy.fintech.pe.core.service.export;

import com.academy.fintech.pe.core.service.agreement.Agreement;
import com.academy.fintech.pe.exporter.client.export.ExportClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private final ExportClientService exportClientService;

    /**
     * Запрос на создание задачи для выгрузки договора в kafka
     */
    public void createTask(Agreement agreement) {
        ExportTask task = ExportTask.builder()
                .type("AGREEMENT")
                .stringKey(agreement.id())
                .build();
        if (!exportClientService.createTask(task))
            log.error("Error create task {}", task);
    }
}
