package com.academy.fintech.puller.core.service.export_task.db;

import com.academy.fintech.puller.core.service.export_task.ExportTask;
import com.academy.fintech.puller.core.service.enumeration.StatusTask;
import com.academy.fintech.puller.core.service.enumeration.ExportType;
import com.academy.fintech.puller.core.service.export_task.db.entity.EntityExportTask;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportTaskService {

    private final ExportTaskRepository exportTaskRepository;

    /**
     * Находит задачи со статусом "NEW", обновляет статус каждой найденной задачи на "PROCESSING" и устанавливает время
     * обновления задачи на текущее время.
     */
    @Transactional
    public List<ExportTask> findByTypeAndStatusNew(ExportType type) {
        List<EntityExportTask> entityExportTasks = exportTaskRepository
                .findByTypeAndStatus(type, StatusTask.NEW);
        LocalDateTime nowTime = LocalDateTime.now();
        entityExportTasks.forEach(task -> {
            task.setStatus(StatusTask.PROCESSING);
            task.setUpdatedAt(nowTime);
        });
        exportTaskRepository.saveAll(entityExportTasks);
        return mapListEntityToTasks(entityExportTasks);
    }

    /**
     * Обновляет статус всем задачам, id которых был передан
     * @param ids лист id задач, которым нужно обновить статус
     */
    @Transactional
    public void setTasksStatus(List<BigInteger> ids, StatusTask newStatus) {
        List<EntityExportTask> entityExportTasks = exportTaskRepository.findAllByIdIn(ids);
        entityExportTasks.forEach(task -> task.setStatus(newStatus));
        exportTaskRepository.saveAll(entityExportTasks);
    }

    public void saveNewTask(ExportTask task) {
        exportTaskRepository.save(createTask(task));
    }

    private List<ExportTask> mapListEntityToTasks(List<EntityExportTask> entityExportTasks) {
        List<ExportTask> exportTasks = new ArrayList<>();
        for (EntityExportTask entityExportTask : entityExportTasks) {
            exportTasks.add(mapEntityToTask(entityExportTask));
        }
        return exportTasks;
    }

    private EntityExportTask createTask(ExportTask task) {
        return EntityExportTask.builder()
                .type(task.type())
                .numberKey(task.numberKey())
                .stringKey(task.stringKey())
                .status(StatusTask.NEW)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ExportTask mapEntityToTask(EntityExportTask entityExportTask) {
        return ExportTask.builder()
                .id(entityExportTask.getId())
                .type(entityExportTask.getType())
                .stringKey(entityExportTask.getStringKey())
                .numberKey(entityExportTask.getNumberKey())
                .build();
    }
}
