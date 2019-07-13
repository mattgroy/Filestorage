package net.borolis.spring.dao;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.borolis.spring.FileStorageException;
import net.borolis.spring.dao.interfaces.CassandraFileDAO;
import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.util.CassandraUtil;

import com.datastax.oss.driver.api.core.NoNodeAvailableException;
import com.datastax.oss.driver.api.core.cql.Row;

/**
 * @see CassandraFileDAO
 * @author bliskov
 * @author mratkov
 * @since July 5, 2019
 */
@Component
public class CassandraFileDAOImpl implements CassandraFileDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraFileDAOImpl.class);

    @Override
    public void delete(final CassandraFile cassandraFile) throws FileStorageException
    {
        try
        {
            LOGGER.info("[Cassandra] Deleting " + cassandraFile);
            CassandraUtil.delete(cassandraFile.getUuid());
            LOGGER.info("[Cassandra] File " + cassandraFile + " deleted");
        }
        catch (NoNodeAvailableException e)
        {
            throw new FileStorageException("[Cassandra] Cannot connect to Cassandra", e);
        }
    }

    @Override
    public void delete(final UUID fileUUID) throws FileStorageException
    {
        try
        {
            delete(getByUUID(fileUUID));
        }
        catch (NoNodeAvailableException e)
        {
            throw new FileStorageException("[Cassandra] Cannot connect to Cassandra", e);
        }
    }

    @Override
    public CassandraFile getByUUID(final UUID fileUUID) throws FileStorageException
    {
        Row row;
        try
        {
            LOGGER.info("[Cassandra] Finding file by UUID: " + fileUUID);
            row = CassandraUtil.getByUUID(fileUUID).one();
        }
        catch (NoNodeAvailableException e)
        {
            throw new FileStorageException("[Cassandra] Cannot connect to Cassandra", e);
        }

        if (row == null)
        {
            LOGGER.info("[Cassandra] File with UUID: " + fileUUID + " not found");
            LOGGER.debug("[Cassandra] Row is null");
            return null;
        }

        CassandraFile cassandraFile = new CassandraFile(fileUUID, row.getBytesUnsafe("file_content"));
        LOGGER.info("[Cassandra] File " + cassandraFile + " found");
        return cassandraFile;
    }

    @Override
    public void saveOrUpdate(final CassandraFile cassandraFile) throws FileStorageException
    {
        LOGGER.info("[Cassandra] Saving file " + cassandraFile);

        if (cassandraFile.getUuid() == null)
        {
            UUID uuid = CassandraUtil.generateUUID();
            LOGGER.info("[Cassandra] Saving file " + cassandraFile + ": UUID " + uuid + " generated");
            cassandraFile.setUuid(uuid);
        }
        try
        {
            CassandraUtil.saveOrUpdate(cassandraFile.getUuid(), cassandraFile.getContent());
            LOGGER.info("[Cassandra] File " + cassandraFile + " saved");
        }
        catch (NoNodeAvailableException e)
        {
            throw new FileStorageException("[Cassandra] Cannot connect to Cassandra", e);
        }
    }
}
