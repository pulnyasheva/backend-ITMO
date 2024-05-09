package com.academy.fintech.puller.core.service.exporter.application;

import com.academy.fintech.puller.core.service.enumeration.ExportType;
import com.academy.fintech.puller.core.service.export_task.ExportTask;
import com.academy.fintech.puller.core.service.export_task.db.ExportTaskService;
import com.academy.fintech.puller.core.service.exporter.AbstractExporter;
import com.academy.fintech.puller.core.service.exporter.application.db.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ApplicationExporter extends AbstractExporter<Application, String> {

    @Autowired
    public ApplicationExporter(ExportTaskService exportTaskService,
                             ApplicationService applicationService,
                             KafkaTemplate<String, Application> kafkaTemplate,
                             @Value("${kafka.exporter.application.topic}") String applicationTopicName
    ) {
        super(exportTaskService, applicationService, kafkaTemplate, applicationTopicName);
    }

    @Override
    protected Function<ExportTask, String> getKey() {
        return ExportTask::stringKey;
    }

    @Override
    protected String getKeyForKafka(Application application) {
        return application.id();
    }

    @Override
    public ExportType getType() {
        return ExportType.APPLICATION;
    }
}
