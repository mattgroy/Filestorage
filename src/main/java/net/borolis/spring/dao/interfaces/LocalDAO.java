package net.borolis.spring.dao.interfaces;

import java.util.Optional;

import net.borolis.spring.exceptions.ResourceNotFoundException;

/**
 * Дженерик интерфейс для работы с объектами в локальной БД
 *
 * @author mratkov
 * @since 9 июля, 2019
 */
public interface LocalDAO<T>
{
    /**
     * Удаление объекта из БД
     *
     * @param entity - объект
     */
    void delete(final T entity);

//    /**
//     * Удаление объекта из БД по id
//     *
//     * @param entityId - id объекта
//     */
//    void deleteBy(final long entityId);

    /**
     * Взятие объекта из БД по id
     *
     * @param id - id объекта
     * @return Entity объект локальной БД
     */
    Optional<T> getBy(final long id);

    /**
     * Обновление или сохранение объекта в БД
     *
     * @param entity - объект
     */
    void save(final T entity);
}
