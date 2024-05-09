package com.academy.fintech.dwh.core.service.conservation.application;

import com.academy.fintech.dwh.core.service.conservation.application.db.ApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerApplication {

    private final ObjectMapper jsonMapper;
    private final ApplicationService applicationService;

    /**
     * Метод предназначен для потребления сообщений из указанного топика kafka и сохранения в базу данных.
     */
    @KafkaListener(topics = "${kafka.consumer.application.topic}",
            groupId = "${kafka.consumer.application.group}",
            batch = "true")
    public void consume(List<ConsumerRecord<String, String>> records,
                        org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        records.stream().map(this::getEvent)
                .forEach(application -> {
                    applicationService.save(application);
                    consumer.commitAsync();
                });
    }

    /**
     * Метод преобразует значение сообщения из json в объект типа {@link Application}.
     */
    private Application getEvent(ConsumerRecord<String, String> record) {
        try {
            return jsonMapper.readValue(record.value(), Application.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json application: {}", record.value());
        }
        return null;
    }
}
