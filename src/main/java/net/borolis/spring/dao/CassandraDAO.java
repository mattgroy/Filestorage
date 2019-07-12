package net.borolis.spring.dao;

import java.util.Optional;
import java.util.UUID;

import net.borolis.spring.entity.CassandraFile;

/**
 * Интерфейс DAO для CassandraFile
 *
 * @author bliskov
 * @since July 5, 2019
 */
public interface CassandraDAO
{
    /**
     * Удалить файл из БД
     *
     * @param cassandraFile file
     */
    void delete(final CassandraFile cassandraFile);

    /**
     * Удалить файл из БД по UUID
     *
     * @param fileUUID UUID of file
     */
    void delete(final UUID fileUUID);

    /**
     * Получить файл из БД по UUID
     *
     * @param fileUUID file UUID
     * @return Optional<CassandraFile>
     */
    Optional<CassandraFile> getByUUID(final UUID fileUUID);

    /**
     * Вставить файл в БД
     *
     * @param cassandraFile - file which need to insert
     * @return Inserted file UUID
     */
    UUID saveOrUpdate(final CassandraFile cassandraFile);
}
