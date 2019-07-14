package net.borolis.spring.exceptions;

/**
 * Исключение общего назначения
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
