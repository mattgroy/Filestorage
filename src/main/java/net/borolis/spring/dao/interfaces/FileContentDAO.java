package net.borolis.spring.dao.interfaces;

import java.util.UUID;

import net.borolis.spring.FileStorageException;
import net.borolis.spring.entity.LocalFileContent;

/**
 * Интерфейс DAO для {@link LocalFileContent}
 *
 * @author mratkov
 * @since 12 июля, 2019
 */
public interface FileContentDAO extends DAO<LocalFileContent>
{
    /**
     * Получить контент файла по cassandraObjectUUID
     *
     * @return {@link LocalFileContent}
     * @throws FileStorageException Объект не найден или ошибка соединения с БД
     */
    LocalFileContent findByUUID(final UUID uuid) throws FileStorageException;
}
