package net.borolis.spring.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.borolis.spring.exceptions.ConnectionFailException;

@RestControllerAdvice
public class FileStorageRestControllerAdvice extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(ConnectionFailException.class)
    public void handleConnectionFailException()
    {
//        return Res
    }
}
