package com.academy.fintech.puller.core.service.export_task;

import com.academy.fintech.exporter.ExporterRequest;
import com.academy.fintech.exporter.ExporterResponse;
import com.academy.fintech.puller.core.service.enumeration.ExportType;
import com.academy.fintech.puller.core.service.export_task.db.ExportTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class ExporterService {

    private final ExportTaskService exportTaskService;

    /**
     * Создает новую задачу для выгрузки в kafka
     */
    public ExporterResponse createTask(ExporterRequest request) {
        ExportTask exportTask = ExportTask.builder()
                .type(ExportType.valueOf(request.getType()))
                .stringKey(request.getStringKey())
                .numberKey(new BigInteger(request.getNumberKey()))
                .build();
        exportTaskService.saveNewTask(exportTask);
        return ExporterResponse.newBuilder()
                .setAccepted(true)
                .build();
    }
}
