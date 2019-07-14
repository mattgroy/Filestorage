package net.borolis.spring.controller;

public class ErrorResponseEntity
{
    private String errorMessage;

    public ErrorResponseEntity(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
