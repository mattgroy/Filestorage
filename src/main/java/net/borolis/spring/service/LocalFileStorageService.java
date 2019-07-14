package net.borolis.spring.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.dao.interfaces.LocalFileContentDAO;
import net.borolis.spring.dao.interfaces.LocalFileDAO;
import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.FileStorageException;
import net.borolis.spring.exceptions.ResourceNotFoundException;
import net.borolis.spring.utils.GeneralUtils;

/**
 * Сервис работы с файлами в локальном хранилище
 *
 * @author bliskov
 * @author mratkov
 * @since July 8, 2019
 */
@Component
public class LocalFileStorageService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileStorageService.class);

    /**
     * Интерфейс для работы с метаинфо файлов в PostgreSQL
     */
    private final LocalFileDAO localFileDAO;

    /**
     * Интерфейс для работы с метаинфо файлов в PostgreSQL
     */
    private final LocalFileContentDAO localFileContentDAO;

    @Autowired
    public LocalFileStorageService(
            final LocalFileDAO localFileDAO,
            final LocalFileContentDAO localFileContentDAO)
    {
        this.localFileDAO = localFileDAO;
        this.localFileContentDAO = localFileContentDAO;
    }

    /**
     * Удаление файла из системы
     *
     * @param localFile {@link LocalFile}
     */
    public void deleteFileMeta(LocalFile localFile)
    {
        localFileDAO.delete(localFile);
    }

    public LocalFile getFileMetaBy(final long fileId) throws ResourceNotFoundException
    {
        return localFileDAO.getBy(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File with id " + fileId + " not found"));
    }

    public Optional<LocalFileContent> getFileContentBy(String hash)
    {
        return localFileContentDAO.getBy(hash);
    }

    /**
     * Получение списка всех файлов
     *
     * @return List of LocalFile
     */
    public Collection<LocalFile> getFilesMeta()
    {
        return localFileDAO.getFiles();
    }

    public Collection<LocalFile> getFilesBy(String hash)
    {
        return localFileDAO.getFilesBy(hash);
    }

    public Collection<LocalFileContent> getFilesContent()
    {
        return localFileContentDAO.getFilesContent();
    }

    /**
     * Сохранение файла в локальную базу данных
     *
     * @param file файл, отправленный пользователем
     */
    public Optional<Long> saveFile(MultipartFile file)
    {
        if (file.isEmpty())
        {
            return Optional.empty();
        }

        byte[] fileBinaryData;
        try
        {
            fileBinaryData = file.getBytes();
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new FileStorageException(e.getMessage(), e);
        }

        String fileHash = GeneralUtils.getSHA256Hash(fileBinaryData);
        LocalFile uploadedFile = new LocalFile(file.getOriginalFilename(), file.getContentType(), fileHash);
        LocalFileContent uploadedFileContent = new LocalFileContent(fileHash, fileBinaryData);
        localFileDAO.save(uploadedFile);
        localFileContentDAO.save(uploadedFileContent);
        return Optional.of(uploadedFile.getId());
    }

    public void deleteFileContentBy(String hash)
    {
        localFileContentDAO.deleteBy(hash);
    }

    public void deleteFileContent(LocalFileContent localFileContent)
    {
        localFileContentDAO.delete(localFileContent);
    }
}
