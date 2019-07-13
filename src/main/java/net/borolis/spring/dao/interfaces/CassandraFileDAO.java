package net.borolis.spring.dao.interfaces;

import java.util.UUID;

import net.borolis.spring.FileStorageException;
import net.borolis.spring.entity.CassandraFile;

/**
 * Интерфейс DAO для CassandraFile
 *
 * @author bliskov
 * @author mratkov
 * @since July 5, 2019
 */
public interface CassandraFileDAO
{
    /**
     * Удалить файл из БД
     *
     * @param cassandraFile {@link CassandraFile}
     * @throws FileStorageException Не удалось подключиться к Cassandra
     */
    void delete(final CassandraFile cassandraFile) throws FileStorageException;

    /**
     * Удалить файл из БД по UUID
     *
     * @param fileUUID UUID of file
     * @throws FileStorageException Не удалось подключиться к Cassandra
     */
    void delete(final UUID fileUUID) throws FileStorageException;

    /**
     * Получить файл из БД по UUID
     *
     * @param fileUUID file UUID
     * @return {@link CassandraFile}
     * @throws FileStorageException Не удалось подключиться к Cassandra
     */
    CassandraFile getByUUID(final UUID fileUUID) throws FileStorageException;

    /**
     * Вставить файл в БД
     *
     * @param cassandraFile {@link CassandraFile}
     * @throws FileStorageException Не удалось подключиться к Cassandra
     */
    void saveOrUpdate(final CassandraFile cassandraFile) throws FileStorageException;
}
