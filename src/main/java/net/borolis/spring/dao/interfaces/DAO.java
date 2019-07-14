package net.borolis.spring.dao.interfaces;

import java.util.Optional;

import net.borolis.spring.exceptions.ResourceNotFoundException;

/**
 * Дженерик интерфейс для работы с объектами в локальной БД
 *
 * @author mratkov
 * @since 9 июля, 2019
 */
public interface DAO<T>
{
    /**
     * Удаление объекта из БД
     *
     * @param entity - объект
     * @throws ResourceNotFoundException Ресурс не найден
     */
    void delete(final T entity) throws ResourceNotFoundException;

    /**
     * Удаление объекта из БД по id
     *
     * @param entityId - id объекта
     * @throws ResourceNotFoundException Ресурс не найден
     */
    void delete(final long entityId) throws ResourceNotFoundException;

    /**
     * Взятие объекта из БД по id
     *
     * @param id - id объекта
     * @return Entity объект локальной БД
     * @throws ResourceNotFoundException Ресурс не найден
     */
    T get(final long id) throws ResourceNotFoundException;

    /**
     * Обновление или сохранение объекта в БД
     *
     * @param entity - объект
     * @throws ResourceNotFoundException Ресурс не найден
     */
    void saveOrUpdate(final T entity) throws ResourceNotFoundException;
}
