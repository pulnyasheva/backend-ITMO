package com.academy.fintech.puller.core.service.dispatcher;

import com.academy.fintech.puller.core.service.enumeration.ExportType;
import com.academy.fintech.puller.core.service.exporter.Exporter;
import com.academy.fintech.puller.core.service.dispatcher.db.ExportTaskTypeInfo;
import com.academy.fintech.puller.core.service.dispatcher.db.ExportTypeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class DispatcherService {

    private final ExportTypeService exportTypeService;

    private final Map<ExportType, Exporter> exporters;

    public DispatcherService(ExportTypeService exportTypeService, List<Exporter> exporters) {
        this.exportTypeService = exportTypeService;

        this.exporters = exporters.stream()
                .collect(Collectors.toMap(Exporter::getType, Function.identity()));
    }

    /**
     * Метод выполняет обновление статусов задач для повторной обработки задач или указания, что выгрузка задачи
     * завершилась ошибкой
     */
    @Scheduled(fixedRateString = "${kafka.exporter.dispatcher.stalled-seconds-rate}", timeUnit = SECONDS)
    public void processStalled() {
        exportTypeService.updateStalledToAnotherRetry();
        exportTypeService.updateStalledToErrorStatus();
    }

    /**
     * Метод выполняет выгрузку задач, которые готовы к обработке
     */
    @Scheduled(fixedRateString = "${kafka.exporter.dispatcher.export-seconds-rate}", timeUnit = SECONDS)
    public void exporter() {
        List<ExportTaskTypeInfo> exportTasksToProcess = exportTypeService.findExportTasks();

        exportTasksToProcess.stream()
                .filter(task -> task.getCountToProcess() > 0)
                .forEach(task -> executeExporter(task.getExportType()));
    }

    private void executeExporter(ExportType exportType) {
        exporters.get(exportType).export();
    }
}
