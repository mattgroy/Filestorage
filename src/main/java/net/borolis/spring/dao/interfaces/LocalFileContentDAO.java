package net.borolis.spring.dao.interfaces;

import java.util.Collection;
import java.util.List;

import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.LocalBDConnectionFailException;
import net.borolis.spring.exceptions.ResourceNotFoundException;

/**
 * Интерфейс DAO для {@link LocalFileContent}
 *
 * @author mratkov
 * @since 12 июля, 2019
 */
public interface LocalFileContentDAO extends DAO<LocalFileContent>
{
    /**
     * Получение контента файла по хешу
     *
     * @param hash Хеш контента
     * @return {@link LocalFileContent}
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     * @throws ResourceNotFoundException Файл не найден
     */
    LocalFileContent get(final String hash) throws LocalBDConnectionFailException, ResourceNotFoundException;

    /**
     * Получение списка хранимого в локальной БД контента
     *
     * @return Список хранимого в локальной БД контента
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     */
    Collection<LocalFileContent> getFileContents() throws LocalBDConnectionFailException;

    /**
     * Удаление хранимого контента по хешу
     *
     * @param hash Хеш контента
     * @return Список с метаинформацией файлов, обладающих удалённым контентом
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     * @throws ResourceNotFoundException Файл не найден
     */
    Collection<LocalFile> deleteByHash(final String hash) throws LocalBDConnectionFailException, ResourceNotFoundException;
}
