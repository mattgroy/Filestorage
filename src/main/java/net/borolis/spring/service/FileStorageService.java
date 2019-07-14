package net.borolis.spring.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.FileStorageException;
import net.borolis.spring.exceptions.ResourceNotFoundException;

@Service
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

    public void deleteFile(final long fileId)
    {
        LocalFile localFile = localFileService.getFile(fileId);
        remoteFileService.deleteFileContent(localFile.getHash());
        localFileService.deleteFile(localFile);
    }

    public ResponseEntity<byte[]> getFileWrapped(long fileId)
    {
        LocalFile localFile = localFileService.getFile(fileId);
        Optional<CassandraFile> cassandraFile = remoteFileService.getByHash(localFile.getHash());

        if (cassandraFile.isPresent())
        {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            String.format("attachment; filename=\"%s\"", localFile.getTitle()))
                    .body(cassandraFile.get().getContent().array());
        }

        LocalFileContent localFileContent = localFile.getFileContent();
        if (localFileContent == null)
        {
            LOGGER.error("Could not find any content of file with id " + fileId);
            throw new FileStorageException("Could not find any content of file with id " + fileId);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", localFile.getTitle()))
                .body(localFileContent.getContent());
    }

    public Collection<LocalFile> getFiles()
    {
        return localFileService.getFiles();
    }

    public Collection<Long> saveAllFilesContent()
    {
        Set<Long> savedFilesIdSet = new HashSet<>();
        List<LocalFileContent> localFileContents = localFileService.getFileContents();
        localFileContents.forEach(localFileContent ->
        {
            savedFilesIdSet.addAll(saveFileContent(localFileContent));
        });


        return savedFilesIdSet;
    }

    public long saveFile(MultipartFile file)
    {
        return 0;
    }

    public Collection<Long> saveFileContent(long fileId)
    {
        Set<Long> savedFilesIdSet = new HashSet<>();
        LocalFileContent localFileContent = localFileService.getFile(fileId).getFileContent();
        if (localFileContent != null)
        {
            savedFilesIdSet.addAll(saveFileContent(localFileContent));
        }
        else
        {
            savedFilesIdSet.add(localFileService.getFiles());
        }

        return savedFilesIdSet;
    }

    // сохраняет в ремоут, удаляет ссылки у все локальных мета
    private Collection<Long> saveFileContent(LocalFileContent localFileContent)
    {

    }
}
