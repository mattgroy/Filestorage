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

import net.borolis.spring.entity.PgFile;
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
    @DeleteMapping(value = "/api/v1/file/{id}")
    public String deleteFile(@PathVariable("id") final long fileId)
    {
        fileService.deleteFile(fileId);
        return "index";
    }

    /**
     * Обработчик для запроса на получение файла
     *
     * @param fileId - id of file
     * @return returns requested file
     */
    @GetMapping(value = "/api/v1/file/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") final long fileId)
    {
        return fileService.getDownloadableFile(fileId);
    }

    /**
     * Обработчик для запроса на получение списка всех файлов
     *
     * @return file list
     */
    @PostMapping(value = "/api/v1/files", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public List<PgFile> getFiles()
    {
        return fileService.getFiles();
    }

    /**
     * Обработчик для события загрузки файла
     *
     * @param file - Uploaded file
     * @return Index page template
     */
    @PostMapping("/upload")
    public String uploadFileHandler(@RequestParam("file") final MultipartFile file)
    {
        fileService.saveFile(file);
        return "index";
    }
}
