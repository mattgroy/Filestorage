package net.borolis.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.borolis.spring.dao.interfaces.CassandraFileDAO;

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
}
