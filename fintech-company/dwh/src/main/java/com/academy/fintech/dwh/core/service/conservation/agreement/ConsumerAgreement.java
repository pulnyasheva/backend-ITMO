package com.academy.fintech.dwh.core.service.conservation.agreement;

import com.academy.fintech.dwh.core.service.conservation.agreement.db.AgreementService;
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
public class ConsumerAgreement {

    private final ObjectMapper jsonMapper;
    private final AgreementService agreementService;

    /**
     * Метод предназначен для потребления сообщений из указанного топика kafka и сохранения в базу данных.
     */
    @KafkaListener(topics = "${kafka.consumer.agreement.topic}",
            groupId = "${kafka.consumer.agreement.group}",
            batch = "true")
    public void consume(List<ConsumerRecord<String, String>> records,
                        org.apache.kafka.clients.consumer.Consumer<?, ?> consumer) {
        records.stream().map(this::getEvent)
                .forEach(agreement -> {
                    agreementService.save(agreement);
                    consumer.commitAsync();
                });
    }

    /**
     * Метод преобразует значение сообщения из json в объект типа {@link Agreement}.
     */
    private Agreement getEvent(ConsumerRecord<String, String> record) {
        try {
            return jsonMapper.readValue(record.value(), Agreement.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json agreement: {}", record.value());
        }
        return null;
    }
}

