package com.academy.fintech.puller.core.service.export_task.db;

import com.academy.fintech.puller.core.service.enumeration.ExportType;
import com.academy.fintech.puller.core.service.enumeration.StatusTask;
import com.academy.fintech.puller.core.service.export_task.db.entity.EntityExportTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface ExportTaskRepository extends JpaRepository<EntityExportTask, BigInteger> {

    List<EntityExportTask> findByTypeAndStatus(ExportType type, StatusTask status);

    List<EntityExportTask> findAllByIdIn(List<BigInteger> ids);
}
