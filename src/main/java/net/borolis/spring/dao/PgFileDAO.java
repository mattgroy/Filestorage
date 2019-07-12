package net.borolis.spring.dao;

import java.util.List;
import java.util.Optional;

import net.borolis.spring.entity.PgFile;

/**
 * Интерфейс DAO для PgFile
 *
 * @author bliskov
 * @since July 5, 2019
 */
public interface PgFileDAO
{
    /**
     * Удалить объект из БД
     *
     * @param pgFile - объект
     */
    void delete(final PgFile pgFile);

    /**
     * Удалить объект из БД
     *
     * @param fileId file id which need to delete
     */
    void delete(final long fileId);

    /**
     * Взять объект из БД по id
     *
     * @param id - id объекта
     * @return PgFile
     */
    Optional<PgFile> getById(final long id);

    /**
     * Получить все файлы
     *
     * @return list of files
     */
    List<PgFile> getFiles();

    /**
     * Обновить или сохранить объект в БД
     *
     * @param pgFile - объект
     */
    void saveOrUpdate(final PgFile pgFile);
}
