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
import net.borolis.spring.service.LocalFileStorageService;

/**
 * Контроллер для работы с файлами
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Controller
public class FileStorageController
{
//    private final FileStorageController fileStorageController;
//
//    @Autowired
//    public FileStorageController(final FileStorageController fileStorageController)
//    {
//        this.fileStorageController = fileStorageController;
//    }
}
