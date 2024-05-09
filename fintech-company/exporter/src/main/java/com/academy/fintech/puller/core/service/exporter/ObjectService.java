package com.academy.fintech.puller.core.service.exporter;

import java.util.List;

/**
 * Интерфейс для классов, обрабатывающих объекты.
 *
 * @param <T> тип объекта
 * @param <G> тип идентификатора объекта
 */
public interface ObjectService<T, G> {

    List<T> getByIds(List<G> ids);
}
