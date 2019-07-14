package net.borolis.spring.dao.interfaces;

import java.util.Collection;
import java.util.Optional;

import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.LocalBDConnectionFailException;
import net.borolis.spring.exceptions.ResourceNotFoundException;

/**
 * Интерфейс DAO для {@link LocalFileContent}
 *
 * @author mratkov
 * @since 12 июля, 2019
 */
public interface LocalFileContentDAO extends LocalDAO<LocalFileContent>
{
    /**
     * Получение контента файла по хешу
     *
     * @param hash Хеш контента
     * @return {@link LocalFileContent}
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     */
    Optional<LocalFileContent> getBy(final String hash) throws LocalBDConnectionFailException;

    /**
     * Получение списка хранимого в локальной БД контента
     *
     * @return Список хранимого в локальной БД контента
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     */
    Collection<LocalFileContent> getFilesContent() throws LocalBDConnectionFailException;

    /**
     * Удаление хранимого контента по хешу
     *
     * @param hash Хеш контента
     * @throws LocalBDConnectionFailException Ошибка соединения с БД
     */
    void deleteBy(final String hash) throws LocalBDConnectionFailException;
}
