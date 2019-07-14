package net.borolis.spring.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
        LocalFile localFile = fileStorageService.getFileMetaBy(fileId);
        fileStorageService.deleteFile(localFile);
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
        LocalFile localFile = fileStorageService.getFileMetaBy(fileId);
        byte[] content = fileStorageService.getFileContent(localFile);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", localFile.getTitle()))
                .body(content);
    }

    /**
     * Обработчик запроса на получение списка всех хранимых файлов
     *
     * @return Список с метаинформацией хранимых файлов
     */
    @GetMapping(value = "/api/v1/files", produces = "application/json")
    public Collection<LocalFile> getFilesMeta()
    {
        return fileStorageService.getFilesMeta();
    }

    /**
     * Обработчик запроса на загрузку контента всех файлов в удалённую БД
     */
    // TODO: 13.07.19  Можно возвращать список с ID загруженных файлов
    @PostMapping("/api/v1/files/locals")
    public void uploadAllFilesContent()
    {
        fileStorageService.saveAllFilesContent();
    }

    /**
     * Обработчик запроса на загрузку файла с контентом в локальную БД
     *
     * @param file Загружаемый файл
     */
    @PostMapping("/api/v1/files")
    public ResponseEntity uploadFileToLocal(@RequestParam("file") final MultipartFile file)
    {
        Optional<Long> id = fileStorageService.saveFile(file);
        if (id.isPresent())
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        return ResponseEntity.badRequest().body("File content is empty");
    }

    /**
     * Обработчик запроса на загрузку контента файла в удалённую БД
     *
     * @param localFileId ID файла в локальной БД
     */
    @PostMapping("/api/v1/files/{id}")
    public Collection<Long> uploadFileToRemote(@PathVariable("id") final long fileId)
    {
        LocalFile localFile = fileStorageService.getFileMetaBy(fileId);
        return fileStorageService.saveFileContent(localFile);
    }
}
