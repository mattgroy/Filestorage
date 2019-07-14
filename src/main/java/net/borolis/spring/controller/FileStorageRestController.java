package net.borolis.spring.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.service.FileStorageService;

/**
 * Контроллер REST-Api файлового хранилища
 *
 * @author mratkov
 * @since 13 июля, 2019
 */
@RestController
public class FileStorageRestController
{
    private final FileStorageService fileStorageService;

    @Autowired
    public FileStorageRestController(final FileStorageService fileStorageService)
    {
        this.fileStorageService = fileStorageService;
    }

    /**
     * Обработчик запроса на удаление файла
     *
     * @param fileId - id of file
     */
    @DeleteMapping(value = "/api/v1/files/{id}")
    public ResponseEntity deleteFile(@PathVariable("id") final long fileId)
    {
        fileStorageService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Обработчик запроса на получение файла
     *
     * @param fileId - id of file
     * @return Запрашиваемый файл, обёрнутый в HTTP-ответ
     */
    @GetMapping(value = "/api/v1/files/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") final long fileId)
    {
        return fileStorageService.getFileWrapped(fileId);
    }

    /**
     * Обработчик запроса на получение списка всех хранимых файлов
     *
     * @return Список с метаинформацией хранимых файлов
     */
    @GetMapping(value = "/api/v1/files", produces = "application/json")
    public Collection<LocalFile> getFiles()
    {
        return fileStorageService.getFiles();
    }

    /**
     * Обработчик запроса на загрузку контента всех файлов в удалённую БД
     *
     * @return Список с ID загруженных файлов
     */
    @PostMapping("/api/v1/files/locals")
    public Collection<Long> uploadAllFilesContent()
    {
        return fileStorageService.saveAllFilesContent();
    }

    /**
     * Обработчик запроса на загрузку файла с контентом в локальную БД
     *
     * @param file Загружаемый файл
     */
    @PostMapping("/api/v1/files")
    public ResponseEntity uploadFileToLocal(@RequestParam("file") final MultipartFile file)
    {
        long id = fileStorageService.saveFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * Обработчик запроса на загрузку контента файла в удалённую БД
     *
     * @param localFileId ID файла в локальной БД
     */
    @PostMapping("/api/v1/files/{id}")
    public Collection<Long> uploadFileToRemote(@PathVariable("id") final long localFileId)
    {
        return fileStorageService.saveFileContent(localFileId);
    }
}
