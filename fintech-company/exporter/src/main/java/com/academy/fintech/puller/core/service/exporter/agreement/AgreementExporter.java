package com.academy.fintech.puller.core.service.exporter.agreement;

import com.academy.fintech.puller.core.service.enumeration.ExportType;
import com.academy.fintech.puller.core.service.export_task.ExportTask;
import com.academy.fintech.puller.core.service.export_task.db.ExportTaskService;
import com.academy.fintech.puller.core.service.exporter.AbstractExporter;
import com.academy.fintech.puller.core.service.exporter.agreement.db.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AgreementExporter extends AbstractExporter<Agreement, String>{

    @Autowired
    public AgreementExporter(ExportTaskService exportTaskService,
                             AgreementService agreementService,
                             KafkaTemplate<String, Agreement> kafkaTemplate,
                             @Value("${kafka.exporter.agreement.topic}") String agreementTopicName
    ) {
        super(exportTaskService, agreementService, kafkaTemplate, agreementTopicName);
    }

    @Override
    protected Function<ExportTask, String> getKey() {
        return ExportTask::stringKey;
    }

    @Override
    protected String getKeyForKafka(Agreement agreement) {
        return agreement.id();
    }

    @Override
    public ExportType getType() {
        return ExportType.AGREEMENT;
    }
}


