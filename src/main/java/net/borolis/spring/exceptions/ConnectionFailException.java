package net.borolis.spring.exceptions;

/**
 * Ошибка, бросаемая в случае отсутствия подключения к БД
 * @author mratkov
 * @since 13 июля, 2019
 */
public class ConnectionFailException extends FileStorageException
{
    public ConnectionFailException(String message)
    {
        super(message);
    }

    public ConnectionFailException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
