package net.borolis.spring.dao.interfaces;

import java.util.List;

import net.borolis.spring.entity.LocalFile;

/**
 * Интерфейс DAO для {@link LocalFile}
 *
 * @see DAO
 * @author mratkov
 * @since 9 июля, 2019
 */
public interface FileMetaDAO extends DAO<LocalFile>
{
    /**
     * Получить мета-информацию всех файлов
     *
     * @return список метаинформации файлов
     */
    List<LocalFile> getFiles();
}
