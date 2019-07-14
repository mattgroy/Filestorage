package net.borolis.spring.dao.interfaces;

import java.util.Optional;

import net.borolis.spring.dao.mappers.CassandraFileProvider;
import net.borolis.spring.entity.CassandraFile;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
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
     * Удаление файла из БД
     *
     * @param cassandraFile {@link CassandraFile}
     */
    @Delete
    void delete(final CassandraFile cassandraFile);

    /**
     * Удаление файла из БД по хешу
     *
     * @param hash Хеш файла
     */
    @Delete(entityClass = CassandraFile.class)
    void delete(final String hash);

    /**
     * Получение файла из БД по хешу
     *
     * @param hash Хеш файла
     * @return {@link CassandraFile}
     */
    @Select
    Optional<CassandraFile> getByHash(final String hash);

    /**
     * Вставка файла в БД
     *
     * @param cassandraFile {@link CassandraFile}
     */
    @Insert(ifNotExists = true)
    void save(final CassandraFile cassandraFile);

    @QueryProvider(providerClass = CassandraFileProvider.class, entityHelpers = CassandraFile.class)
    boolean isHashStored(final String hash);
}
