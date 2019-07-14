package net.borolis.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.borolis.spring.exceptions.ConnectionFailException;
import net.borolis.spring.exceptions.FileStorageException;
import net.borolis.spring.exceptions.ResourceNotFoundException;

import com.datastax.oss.driver.api.core.NoNodeAvailableException;

/**
 * Обработчик исключений приложения
 *
 * @author mratkov
 * @since 13 июля, 2019
 */
@RestControllerAdvice
public class FileStorageRestControllerAdvice extends ResponseEntityExceptionHandler
{
    /**
     * Обработчик исключения {@link ConnectionFailException}
     * @param exception Отловленное исключение
     * @return REST-ответ
     */
    // FIXME: 13.07.19 Данные исключения не кидаются, т.к.
    //  1) Не знаю как ловить ошибку соединения к постгресу
    //  2) Реализация CassandraDAO генерируется при билде приложения -> кидается NoNodeAvailableException
    @ExceptionHandler(ConnectionFailException.class)
    public ResponseEntity<String> handleConnectionFailException(ConnectionFailException exception)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    /**
     * Обработчик исключения {@link NoNodeAvailableException}
     * @param exception Отловленное исключение
     * @return REST-ответ
     */
    @ExceptionHandler(NoNodeAvailableException.class)
    public ResponseEntity<String> handleCassandraConnectionFailException(NoNodeAvailableException exception)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[Cassandra] Ошибка подключения");
    }

    /**
     * Обработчик исключения {@link ResourceNotFoundException}
     * @param exception Отловленное исключение
     * @return REST-ответ
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    // FIXME: 13.07.19 попробовать поменять на RuntimeException или FileStorageException
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException exception)
    {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity handleFileStorageException(FileStorageException exception)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
