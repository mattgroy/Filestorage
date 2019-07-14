package net.borolis.spring.exceptions;

public class ResourceNotFoundException extends FileStorageException
{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
