package com.academy.fintech.pe.exporter.client.export;

import com.academy.fintech.exporter.ExporterRequest;
import com.academy.fintech.pe.core.service.export.ExportTask;
import com.academy.fintech.pe.exporter.client.export.grpc.ExportGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExportClientService {

    private final ExportGrpcClient exportGrpcClient;

    /**
     * Запрос на создание задачи для выгрузки в kafka
     *
     * @return результат создания задачи, где указано создалась ли задача
     */
    public boolean createTask(ExportTask task) {
        return exportGrpcClient.createTask(mapTaskToRequest(task)).getAccepted();
    }

    private ExporterRequest mapTaskToRequest(ExportTask task) {
        return ExporterRequest.newBuilder()
                .setType(task.type())
                .setNumberKey(task.numberKey().toString())
                .setStringKey(task.stringKey())
                .build();
    }
}
