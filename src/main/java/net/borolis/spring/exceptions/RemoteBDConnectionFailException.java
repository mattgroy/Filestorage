package net.borolis.spring.exceptions;

/**
 * Ошибка, бросаемая в случае отсутствия подключения к удалённой БД
 * @author mratkov
 * @since 13 июля, 2019
 */
public class RemoteBDConnectionFailException extends ConnectionFailException
{
    public RemoteBDConnectionFailException(String message)
    {
        super(message);
    }

    public RemoteBDConnectionFailException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
