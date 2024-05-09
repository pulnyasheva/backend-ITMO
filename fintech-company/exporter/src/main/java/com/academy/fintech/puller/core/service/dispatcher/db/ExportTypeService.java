package com.academy.fintech.puller.core.service.dispatcher.db;

import com.academy.fintech.puller.core.service.dispatcher.property.ExportTaskRetryStalledProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportTypeService {

    private final ExportTypeRepository exportTypeRepository;

    private final ExportTaskRetryStalledProperty retryStalledProperty;

    /**
     * Получает количество задач для каждого типа.
     *
     * @return список объектов {@link ExportTaskTypeInfo}, содержащих информацию о типе и количестве задач с данным
     * типом.
     */
    public List<ExportTaskTypeInfo> findExportTasks() {
        return exportTypeRepository.findExportTasks();
    }


    /**
     * Этот метод обновляет задачи, находящиеся в состоянии "PROCESSING", до статуса "NEW", если прошло время для
     * выполнения задачи и количество повторных обработок не достигло максимума.
     * После обновления задач, метод выводит предупреждение с перечислением идентификаторов обновленных задач.
     */
    public void updateStalledToAnotherRetry() {
        List<BigInteger> retriedTaskIds = exportTypeRepository
                .updateStalledToAnotherRetry(retryStalledProperty.passedMinutesInProcessingToRetry());

        if (!retriedTaskIds.isEmpty()) {
            log.warn("Retried stalled export tasks: {}", retriedTaskIds);
        }
    }

    /**
     * Этот метод обновляет задачи, находящиеся в состоянии "PROCESSING", до статуса "ERROR", если прошло время для
     * выполнения задачи и количество повторных обработок достигло максимума.
     * После обновления задач, метод выводит предупреждение с перечислением идентификаторов обновленных задач.
     */
    public void updateStalledToErrorStatus() {
        List<BigInteger> failedTaskIds = exportTypeRepository
                .updateStalledToErrorStatus(retryStalledProperty.passedMinutesInProcessingToRetry());

        if (!failedTaskIds.isEmpty()) {
            log.error("Export tasks moved to ERROR status: {}", failedTaskIds);
        }
    }

}
