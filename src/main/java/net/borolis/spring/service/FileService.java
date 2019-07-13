package net.borolis.spring.service;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.FileStorageException;
import net.borolis.spring.dao.interfaces.CassandraFileDAO;
import net.borolis.spring.dao.interfaces.FileMetaDAO;
import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.util.CassandraUtil;

/**
 * Сервис работы с файлами
 *
 * @author bliskov
 * @author mratkov
 * @since July 8, 2019
 */
//TODO: отдельный метод для загрузки контента всех файлов в удаленную БД
@Component
public class FileService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    /**
     * Интерфейс для работы с метаинфо файлов в PostgreSQL
     */
    private final FileMetaDAO fileMetaDAO;

    /**
     * Интерфейс для работы с файлами в Cassandra
     */
    private final CassandraFileDAO cassandraFileDAO;

    @Autowired
    public FileService(
            final FileMetaDAO fileMetaDAO,
            final CassandraFileDAO cassandraFileDAO)
    {
        this.fileMetaDAO = fileMetaDAO;
        this.cassandraFileDAO = cassandraFileDAO;
    }

    /**
     * Удаление файла из системы
     *
     * @param fileId file id
     */
    public ResponseEntity deleteFile(long fileId)
    {
        try
        {
            LocalFile pgFile = fileMetaDAO.getById(fileId);
            if (pgFile.getContent() == null)
                cassandraFileDAO.delete(pgFile.getCassandraObjectId());
            fileMetaDAO.delete(pgFile);
            return ResponseEntity.ok(null);
        }
        catch (FileStorageException e)
        {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Получить файл по id
     *
     * @param fileId file id
     * @return Downloadable Entity with file
     */
    public ResponseEntity<byte[]> getFileContent(final long fileId)
    {
        LocalFile pgLocalFile;
        CassandraFile cassandraFile;

        try
        {
            pgLocalFile = fileMetaDAO.getById(fileId);
            cassandraFile = cassandraFileDAO.getByUUID(pgLocalFile.getCassandraObjectId());
        }
        catch (FileStorageException e)
        {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        if (cassandraFile != null)
        {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            String.format("attachment; filename=\"%s\"", pgLocalFile.getTitle()))
                    .body(cassandraFile.getContent().array());
        }

        if (pgLocalFile.getContent() != null)
        {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            String.format("attachment; filename=\"%s\"", pgLocalFile.getTitle()))
                    .body(pgLocalFile.getContent());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * Получение списка всех файлов
     *
     * @return List of LocalFile
     */
    public List<LocalFile> getFiles()
    {
        return fileMetaDAO.getFiles();
    }

    /**
     * Сохранение контента всех файлов из локальной БД в удалённую БД (если их там нет)
     */
    public ResponseEntity saveAllFilesContentToRemote()
    {
        try
        {
            List<LocalFile> files = getFiles();
            files.forEach(this::saveFileContentToRemote);
            return ResponseEntity.ok(null);
        }
        catch (FileStorageException e)
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
    public ResponseEntity saveFileContentToRemote(final long id)
    {
        try
        {
            LocalFile pgLocalFile = fileMetaDAO.getById(id);
            saveFileContentToRemote(pgLocalFile);
            return ResponseEntity.ok(null);
        }
        catch (FileStorageException e)
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
    public ResponseEntity saveFileLocally(MultipartFile file)
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

        UUID cassandraFileUUID = CassandraUtil.generateUUID();
        LocalFile uploadedLocalFile = new LocalFile(
                file.getOriginalFilename(),
                file.getContentType(),
                cassandraFileUUID,
                fileBinaryData
        );

        fileMetaDAO.saveOrUpdate(uploadedLocalFile);
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
        if (localFile.getContent() == null)
            return;
        ByteBuffer fileBinaryData = ByteBuffer.wrap(localFile.getContent());
        cassandraFileDAO.saveOrUpdate(new CassandraFile(localFile.getCassandraObjectId(), fileBinaryData));
        localFile.setContent(null);
        fileMetaDAO.saveOrUpdate(localFile);
    }
}
