package net.borolis.spring.dao.interfaces;

/**
 * Дженерик интерфейс для работы с объектами в локальной БД
 * @author mratkov
 * @since 9 июля, 2019
 */
public interface DAO<T>
{
    /**
     * Удалить объект из БД
     *
     * @param entity - объект
     */
    void delete(final T entity);

    /**
     * Удалить объект из БД по id
     *
     * @param entityId - id объекта
     */
    void delete(final long entityId);

    /**
     * Взять объект из БД по id
     *
     * @param id - id объекта
     * @return Entity объект локальной БД
     */
    T getById(final long id);

    /**
     * Обновить или сохранить объект в БД
     *
     * @param entity - объект
     */
    void saveOrUpdate(final T entity);
}
