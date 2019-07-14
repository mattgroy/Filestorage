package net.borolis.spring.dao.interfaces;

import java.util.Collection;

import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.exceptions.LocalBDConnectionFailException;

/**
 * Интерфейс DAO для {@link LocalFile}
 *
 * @see LocalDAO
 * @author mratkov
 * @since 9 июля, 2019
 */
public interface LocalFileDAO extends LocalDAO<LocalFile>
{
    /**
     * Получение метаинформации всех хранимых файлов
     *
     * @return Список с метаинформацией файлов
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     */
    Collection<LocalFile> getFiles() throws LocalBDConnectionFailException;

    /**
     * Получение метаинформации всех файлов, у которых контент обладает указанным хешем
     *
     * @return Список с метаинформацией файлов
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     */
    Collection<LocalFile> getFilesBy(String hash) throws LocalBDConnectionFailException;
}
