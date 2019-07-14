package net.borolis.spring.service;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.exceptions.FileStorageException;
import net.borolis.spring.dao.interfaces.LocalFileContentDAO;
import net.borolis.spring.dao.interfaces.LocalFileDAO;
import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.utils.GeneralUtils;

import com.datastax.oss.driver.api.core.NoNodeAvailableException;

/**
 * Сервис работы с файлами в локальном хранилище
 *
 * @author bliskov
 * @author mratkov
 * @since July 8, 2019
 */
@Service
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
    public void deleteFile(LocalFile localFile)
    {
        if (localFile.getFileContent() != null)
        {
            LocalFileContent localFileContent = localFile.getFileContent();
            localFileContent.unlinkFileMeta(localFile);
            tryDeleteFileContent(localFileContent);
        }
        localFileDAO.delete(localFile);
    }

    public LocalFile getFile(final long fileId)
    {
        return localFileDAO.get(fileId);
    }

    /**
     * Получение списка всех файлов
     *
     * @return List of LocalFile
     */
    public Collection<LocalFile> getFiles()
    {
        return localFileDAO.getFiles();
    }

    public Collection<LocalFile> getFiles(String hash)
    {
        return localFileDAO.getFiles(hash);
    }

    public Collection<LocalFileContent> getFileContents()
    {
        return localFileContentDAO.getFileContents();
    }

    /**
     * Сохранение контента всех файлов из локальной БД в удалённую БД (если их там нет)
     */
    public Set<Long> saveAllFilesContentToRemote()
    {
        try
        {
            List<LocalFile> files = getFiles();
            files.forEach(this::saveFileContentToRemote);
            return ResponseEntity.ok(null);
        }
        catch (FileStorageException | NoNodeAvailableException e)
        {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Сохранение файла в удалённую БД
     *
     * @param id id файла в локальной БД
     */
    public void saveFileContentToRemote(final long id)
    {
        try
        {
            LocalFile pgLocalFile = localFileDAO.get(id);
            saveFileContentToRemote(pgLocalFile);
            return ResponseEntity.ok(null);
        }
        catch (FileStorageException | NoNodeAvailableException e)
        {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Сохранение файла в локальную базу данных
     *
     * @param file файл, отправленный пользователем
     */
    public long saveFile(MultipartFile file)
    {
        if (file.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        byte[] fileBinaryData;
        try
        {
            fileBinaryData = file.getBytes();
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        UUID cassandraFileUUID = GeneralUtils.generateUUID();
        LocalFile uploadedLocalFile = new LocalFile(
                file.getOriginalFilename(),
                file.getContentType(),
                cassandraFileUUID
        );
        LocalFileContent uploadedLocalFileContent = new LocalFileContent(cassandraFileUUID, fileBinaryData);

        localFileDAO.saveOrUpdate(uploadedLocalFile);
        localFileContentDAO.saveOrUpdate(uploadedLocalFileContent);
        return ResponseEntity
                .created(URI.create(String.format("/api/v1/files/%s", uploadedLocalFile.getId())))
                .body(null);
    }

    /**
     * Сохранение файла в удалённую БД
     *
     * @param localFile {@link LocalFile}
     */
    private void saveFileContentToRemote(final LocalFile localFile)
    {
        LocalFileContent fileContent;
        try
        {
            fileContent = localFileContentDAO.get(localFile.getHash());
        }
        catch (FileStorageException e)
        {
            LOGGER.error(e.getMessage(), e);
            return;
        }

        ByteBuffer fileBinaryData = ByteBuffer.wrap(fileContent.getContent());
        cassandraFileDAO.saveOrUpdate(new CassandraFile(localFile.getHash(), fileBinaryData));
        localFileContentDAO.delete(fileContent);
    }

    private void tryDeleteFileContent(LocalFileContent localFileContent)
    {
        if (localFileContent.getLocalFilesMeta().size() == 0)
            localFileContentDAO.delete(localFileContent);
    }
}
