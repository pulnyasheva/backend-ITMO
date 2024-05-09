package com.academy.fintech.origination.core.service.export;

import com.academy.fintech.origination.core.exporter.client.export.ExportClientService;
import com.academy.fintech.origination.core.service.application.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private final ExportClientService exportClientService;

    /**
     * Запрос на создание задачи для выгрузки заявки в kafka
     */
    public void createTask(Application application) {
        ExportTask task = ExportTask.builder()
                .type("APPLICATION")
                .stringKey(application.id())
                .build();
        if (!exportClientService.createTask(task))
            log.error("Error create task {}", task);
    }
}
