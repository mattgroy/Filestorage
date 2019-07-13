package net.borolis.spring;

/**
 * Ошибка, бросаемая в случаях, когда запрос к БД не удался вследствие отсутствия подключения или иных непредвиденных
 * факторов
 * @author mratkov
 * @since 11 июля, 2019
 */
public class FileStorageException extends RuntimeException
{
    public FileStorageException(String message)
    {
        super(message);
    }

    public FileStorageException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
