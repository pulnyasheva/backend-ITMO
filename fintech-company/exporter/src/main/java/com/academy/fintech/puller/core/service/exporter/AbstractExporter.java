package com.academy.fintech.puller.core.service.exporter;

import com.academy.fintech.puller.core.service.enumeration.StatusTask;
import com.academy.fintech.puller.core.service.export_task.ExportTask;
import com.academy.fintech.puller.core.service.export_task.db.ExportTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Абстрактный класс, реализующий интерфейс Exporter и предоставляющий базовую функциональность
 * для выгрузки объектов в kafka.
 *
 * @param <T> тип объекта для выгрузки
 * @param <G> тип идентификатора объекта
 */
@Slf4j
public abstract class AbstractExporter<T, G> implements Exporter {

    private final ExportTaskService exportTaskService;
    private final ObjectService<T, G> objectService;
    private final KafkaTemplate<String, T> kafkaTemplate;
    private final String topicName;

    public AbstractExporter(ExportTaskService exportTaskService,
                            ObjectService<T, G> objectService,
                            KafkaTemplate<String, T> kafkaTemplate,
                            String topicName
    ) {
        this.exportTaskService = exportTaskService;
        this.objectService = objectService;
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    /**
     * Выполняет выгрузку задач в kafka.
     * Находит все задачи определенного типа, которые готовы к выгрузке, выгружает их в kafka и изменяет задачам статус
     * на "SUCCESS"
     */
    @Override
    public void export() {
        List<ExportTask> tasks = exportTaskService.findByTypeAndStatusNew(getType());

        List<G> objectIds = tasks.stream().map(getKey()).toList();

        CompletableFuture[] futures = objectService.getByIds(objectIds).stream()
                .map(object -> kafkaTemplate.send(topicName, getKeyForKafka(object), object))
                .toArray(CompletableFuture[]::new);

        waitSendingEnd(CompletableFuture.allOf(futures), tasks.size());

        setStatusForSuccess(tasks);
    }

    /**
     * Метод получает функцию для получения id объекта из задачи.
     */
    protected abstract Function<ExportTask, G> getKey();

    /**
     * Метод получает ключ для выгрузки задачи в kafka
     *
     * @param t объект, который нужно выгрузить
     * @return полученный ключ в виде строки
     */
    protected abstract String getKeyForKafka(T t);

    /**
     * Метод выполняет ожидание выгрузки элементов в kafka
     */
    private void waitSendingEnd(CompletableFuture<Void> commonCompletableFuture, int countSendingElements) {
        //  Ожидание максимальное значение ожидания для отправки kafka + одна минута на сериализацию
        //  и отображение данных
        final long awaitTime = countSendingElements * 180000L + 60000;

        try {
            commonCompletableFuture.get(awaitTime, MILLISECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Изменяет всем задачам статус на "SUCCESS"
     */
    private void setStatusForSuccess(List<ExportTask> tasks) {
        List<BigInteger> taskIds = tasks.stream()
                .map(ExportTask::id).toList();
        exportTaskService.setTasksStatus(taskIds, StatusTask.SUCCESS);
    }

}
