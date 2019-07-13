package net.borolis.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.service.FileService;

/**
 * Контроллер для работы с файлами
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Controller
public class FileController
{
    private final FileService fileService;

    @Autowired
    public FileController(final FileService fileService)
    {
        this.fileService = fileService;
    }

    /**
     * Обработчик для запроса на удаление файла
     *
     * @param fileId - id of file
     * @return Index page template
     */
    @DeleteMapping(value = "/api/v1/files/{id}")
    @ResponseBody
    public ResponseEntity deleteFile(@PathVariable("id") final long fileId)
    {
        return fileService.deleteFile(fileId);
    }

    /**
     * Обработчик для запроса на получение файла
     *
     * @param fileId - id of file
     * @return returns requested file
     */
    @GetMapping(value = "/api/v1/files/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") final long fileId)
    {
        return fileService.getFileContent(fileId);
    }

    /**
     * Обработчик для запроса на получение списка всех файлов
     *
     * @return file list
     */
    @GetMapping(value = "/api/v1/files", produces = "application/json")
    @ResponseBody
    public List<LocalFile> getFiles()
    {
        return fileService.getFiles();
    }

    /**
     * Обработчик для события загрузки контента всех файлов в удалённую БД
     *
     * @author mratkov
     * @since 9 июля, 2019
     */
    @PostMapping("/api/v1/files/locals")
    @ResponseBody
    public ResponseEntity uploadAllFilesContentHandler()
    {
        return fileService.saveAllFilesContentToRemote();
    }

    /**
     * Обработчик для события загрузки файла с контентом в локальную БД
     *
     * @param file - Uploaded file
     * @return Index page template
     */
    @PostMapping("/api/v1/files")
    public String uploadFileLocally(@RequestParam("file") final MultipartFile file)
    {
        fileService.saveFileLocally(file);
        return "redirect:/";
    }

    /**
     * Обработчик для загрузки всех файлов в удалённую БД
     *
     * @param fileId id файла в локальной БД
     * @return {@link ResponseEntity}
     */
    @PostMapping("/api/v1/files/{id}")
    @ResponseBody
    public ResponseEntity uploadFileToRemote(@PathVariable("id") final long fileId)
    {
        return fileService.saveFileContentToRemote(fileId);
    }
}
