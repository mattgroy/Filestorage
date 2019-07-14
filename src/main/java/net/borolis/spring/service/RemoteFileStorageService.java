package net.borolis.spring.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.borolis.spring.dao.interfaces.CassandraFileDAO;
import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.ResourceNotFoundException;

/**
 * Сервис работы с файлами в удалённом хранилище
 *
 * @author mratkov
 * @since 13 июля, 2019
 */
@Service
public class RemoteFileStorageService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteFileStorageService.class);

    /**
     * Интерфейс для работы с файлами в Cassandra
     */
    private final CassandraFileDAO cassandraFileDAO;

    @Autowired
    public RemoteFileStorageService(CassandraFileDAO cassandraFileDAO)
    {
        this.cassandraFileDAO = cassandraFileDAO;
    }

    public void deleteFileContent(String hash)
    {
        cassandraFileDAO.delete(hash);
    }

//    public long[] saveAllFilesContent()
//    {
//        List<LocalFile> files = localFileStorageService.getFiles();
//        files.forEach(localFile ->
//        {
//            saveFileContent(localFile.getFileContent())
//        });
//        return ResponseEntity.ok(null);
//    }

    public void saveFileContent(long localFileId)
    {
    }

    public Optional<CassandraFile> getByHash(String hash)
    {
        Optional<CassandraFile> optionalCassandraFile = cassandraFileDAO.getByHash(hash);
        if (!optionalCassandraFile.isPresent())
            LOGGER.info("[Cassandra] Could not find a file with hash " + hash);
        return optionalCassandraFile;
    }
}
