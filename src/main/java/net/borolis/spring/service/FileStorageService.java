package net.borolis.spring.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.FileStorageException;

@Component
public class FileStorageService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    private final LocalFileStorageService localFileService;
    private final RemoteFileStorageService remoteFileService;

    @Autowired
    public FileStorageService(
            final LocalFileStorageService localFileService,
            final RemoteFileStorageService remoteFileService)
    {
        this.localFileService = localFileService;
        this.remoteFileService = remoteFileService;
    }

    public void deleteFile(LocalFile localFile)
    {
        if (localFileService.getFilesBy(localFile.getHash()).size() == 1)
        {
            remoteFileService.deleteFileContentBy(localFile.getHash());
            localFileService.deleteFileContentBy(localFile.getHash());
        }
        localFileService.deleteFileMeta(localFile);
    }

    public LocalFile getFileMetaBy(final long fileId)
    {
        return localFileService.getFileMetaBy(fileId);
    }

    public byte[] getFileContent(LocalFile localFile)
    {
        byte[] content;

        Optional<LocalFileContent> localFileContent = localFileService.getFileContentBy(localFile.getHash());
        if (localFileContent.isPresent())
        {
            content = localFileContent.get().getContent();
        }
        else
        {
            Optional<CassandraFile> cassandraFile = remoteFileService.getByHash(localFile.getHash());
            if (cassandraFile.isPresent())
            {
                content = cassandraFile.get().getContent().array();
            }
            else
            {
                LOGGER.error("Could not find any content of file with id " + localFile.getId());
                throw new FileStorageException("Could not find any content of file with id " + localFile.getId());
            }
        }

        return content;
    }

    public Collection<LocalFile> getFilesMeta()
    {
        return localFileService.getFilesMeta();
    }

    // TODO: 13.07.19  Можно возвращать список с ID загруженных файлов
    public void saveAllFilesContent()
    {
        Collection<LocalFileContent> localFilesContent = localFileService.getFilesContent();
        localFilesContent.forEach(this::saveFileContent);
    }

    public Optional<Long> saveFile(MultipartFile file)
    {
        return localFileService.saveFile(file);
    }

    public Collection<Long> saveFileContent(LocalFile localFile)
    {
        Set<Long> savedFilesIdSet = new HashSet<>();
        Optional<LocalFileContent> localFileContent = localFileService.getFileContentBy(localFile.getHash());
        localFileContent.ifPresent(this::saveFileContent);
        Collection<LocalFile> filesWithHash = localFileService.getFilesBy(localFile.getHash());
        filesWithHash.forEach(localFile1 -> savedFilesIdSet.add(localFile.getId()));
        return savedFilesIdSet;
    }

    private void saveFileContent(LocalFileContent localFileContent)
    {
        remoteFileService.saveFileContent(localFileContent);
        localFileService.deleteFileContent(localFileContent);
    }
}
