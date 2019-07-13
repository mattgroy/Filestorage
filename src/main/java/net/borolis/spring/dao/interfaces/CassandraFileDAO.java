package net.borolis.spring.dao.interfaces;

import java.util.UUID;

import net.borolis.spring.entity.CassandraFile;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

/**
 * Интерфейс DAO для CassandraFile
 *
 * @author bliskov
 * @author mratkov
 * @since July 5, 2019
 */
@Dao
public interface CassandraFileDAO
{
    /**
     * Удалить файл из БД
     *
     * @param cassandraFile {@link CassandraFile}
     */
    @Delete
    void delete(final CassandraFile cassandraFile);

    /**
     * Удалить файл из БД по UUID
     *
     * @param fileUUID UUID of file
     */
    @Delete(entityClass = CassandraFile.class)
    void delete(final UUID fileUUID);

    /**
     * Получить файл из БД по UUID
     *
     * @param fileUUID file UUID
     * @return {@link CassandraFile}
     */
    @Select
    CassandraFile getByUUID(final UUID fileUUID);

    /**
     * Вставить файл в БД
     *
     * @param cassandraFile {@link CassandraFile}
     */
    @Insert
    void saveOrUpdate(final CassandraFile cassandraFile);
}
