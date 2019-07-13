package net.borolis.spring.exceptions;

/**
 * Ошибка, бросаемая в случае отсутствия подключения к локальной БД
 * @author mratkov
 * @since 13 июля, 2019
 */
public class LocalBDConnectionFailException extends ConnectionFailException
{
    public LocalBDConnectionFailException(String message)
    {
        super(message);
    }

    public LocalBDConnectionFailException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
