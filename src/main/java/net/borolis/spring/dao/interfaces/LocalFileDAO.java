package net.borolis.spring.dao.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.exceptions.LocalBDConnectionFailException;

/**
 * Интерфейс DAO для {@link LocalFile}
 *
 * @see DAO
 * @author mratkov
 * @since 9 июля, 2019
 */
public interface LocalFileDAO extends DAO<LocalFile>
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
    Collection<LocalFile> getFiles(String hash) throws LocalBDConnectionFailException;
}
