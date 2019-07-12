package net.borolis.spring.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.dao.CassandraDAO;
import net.borolis.spring.dao.PgFileDAO;
import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.entity.PgFile;

/**
 * Сервис работы с файлами
 *
 * @author bliskov
 * @since July 8, 2019
 */
@Component
public class FileService
{
    private final PgFileDAO pgFileDAO;
    private final CassandraDAO cassandraDAO;

    @Autowired
    public FileService(final PgFileDAO pgFileDAO, final CassandraDAO cassandraDAO)
    {
        this.pgFileDAO = pgFileDAO;
        this.cassandraDAO = cassandraDAO;
    }

    /**
     * Удаление файла из системы
     *
     * @param fileId file id
     */
    public void deleteFile(long fileId)
    {
        Optional<PgFile> optionalPgFile = pgFileDAO.getById(fileId);
        optionalPgFile.ifPresent(pgFile -> cassandraDAO.delete(pgFile.getCassandraObjectId()));
        pgFileDAO.delete(fileId);
    }

    /**
     * Получение списка всех файлов
     *
     * @return List of PgFile
     */
    public List<PgFile> getFiles()
    {
        return pgFileDAO.getFiles();
    }

    /**
     * Сохранение файла в системе
     *
     * @param file file from user
     */
    public void saveFile(MultipartFile file)
    {
        if (file.isEmpty())
        {
            return;
        }

        ByteBuffer fileBinaryData;
        try
        {
            fileBinaryData = ByteBuffer.wrap(file.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        CassandraFile cassandraFile = new CassandraFile(fileBinaryData);
        UUID cassandraFileUUID = cassandraDAO.saveOrUpdate(cassandraFile);

        PgFile uploadedFile = new PgFile(file.getOriginalFilename(), file.getContentType(), cassandraFileUUID);
        pgFileDAO.saveOrUpdate(uploadedFile);
    }

    /**
     * Получить файл по id
     *
     * @param fileId file id
     * @return Downloadable Entity with file
     */
    public ResponseEntity<byte[]> getDownloadableFile(final long fileId)
    {
        Optional<PgFile> optionalPgFile = pgFileDAO.getById(fileId);

        if (optionalPgFile.isPresent())
        {
            final PgFile pgFile = optionalPgFile.get();
            final Optional<CassandraFile> optionalCassandraFile = cassandraDAO.getByUUID(pgFile.getCassandraObjectId());
            if (optionalCassandraFile.isPresent())
            {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                String.format("attachment; filename=\"%s\"", pgFile.getTitle()))
                        .body(optionalCassandraFile.get().getContent().array());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
