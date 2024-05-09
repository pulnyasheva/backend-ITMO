package com.academy.fintech.puller.core.service.dispatcher.db;

import com.academy.fintech.puller.core.service.export_task.db.entity.EntityExportTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ExportTypeRepository extends JpaRepository<EntityExportTask, BigInteger> {

    /**
     * Метод выполняет SQL-запрос для обновления статуса задачи, которая находится в состоянии "PROCESSING".
     * Если задача находится в этом состоянии более указанного количества минут {@code passedMinutesInProcessingToRetry},
     * и количество повторных попыток обработки задачи ещё не достигло допустимого максимума,
     * то ей присваивается статус "NEW" и увеличивается счетчик повторных попыток выполнения задачи.
     *
     * @param passedMinutesInProcessingToRetry количество минут, после которых задача требует повторной попытки
     *                                         обработки
     * @return список идентификаторов задач, которые были обновлены
     */
    @Modifying
    @Query(value = """
            with stalled as (
                    select task.id
                      from export_tasks task
                      join export_types type on type.code = task.type
                     where extract(epoch from clock_timestamp() - task.updated_at) / 60 >= :passedMinutesInProcessingToRetry
                       and task.status = 'PROCESSING'
                       and task.retries_count < type.retry_count
                )
                update export_tasks ue
                   set status = 'NEW',
                       updated_at = clock_timestamp(),
                       retries_count = retries_count + 1
                  from stalled se
                 where ue.id = se.id
                returning ue.id
            """, nativeQuery = true)
    List<BigInteger> updateStalledToAnotherRetry(
            @Param("passedMinutesInProcessingToRetry") int passedMinutesInProcessingToRetry);


    /**
     * Метод выполняет SQL-запрос для обновления статуса задачи, которая находится в состоянии "PROCESSING".
     * Если задача находится в этом состоянии более указанного количества минут {@code passedMinutesInProcessingToRetry},
     * и количество повторных попыток обработки задачи достигло допустимого максимума,
     * то ей присваивается статус "ERROR" и увеличивается счетчик повторных попыток выполнения задачи.
     *
     * @param passedMinutesInProcessingToRetry количество минут, после которых задача требует повторной попытки
     *                                         обработки
     * @return список идентификаторов задач, которые были обновлены
     */
    @Modifying
    @Query(value = """
            with stalled as (
                select task.id
                  from export_tasks task
                  join export_types type on type.code = task.type
                 where extract(epoch from clock_timestamp() - task.updated_at) / 60
                 >= :passedMinutesInProcessingToRetry
                   and task.status = 'PROCESSING'
                   and task.retries_count >= type.retry_count
            )
            update export_tasks ue
               set status = 'ERROR',
                   updated_at = clock_timestamp()
              from stalled se
             where ue.id = se.id
            returning ue.id
            """, nativeQuery = true)
    List<BigInteger> updateStalledToErrorStatus(
            @Param("passedMinutesInProcessingToRetry") int passedMinutesInProcessingToRetry);

    /**
     * Метод выполняет SQL-запрос для поиска информации о задачах.
     * Получает количество задач для каждого типа.
     *
     * @return список объектов {@link ExportTaskTypeInfo}, содержащих информацию о типе и количестве задач с данным
     * типом.
     */
    @Query(value = """
            select et.type     export_type,
                   count(t.id) count_to_process
              from (select type
              		  from export_tasks
             		 group by type) et
             inner join export_tasks t on t.type = et.type
             						 and t.status = 'NEW'
             group by et.type
            """, nativeQuery = true)
    List<ExportTaskTypeInfo> findExportTasks();
}
