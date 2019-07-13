package net.borolis.spring.dao;

import java.util.UUID;

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
    @Override
    public void delete(final CassandraFile cassandraFile) throws FileStorageException
    {
        try
        {
            CassandraUtil.delete(cassandraFile.getUuid());
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
            row = CassandraUtil.getByUUID(fileUUID).one();
        }
        catch (NoNodeAvailableException e)
        {
            throw new FileStorageException("[Cassandra] Cannot connect to Cassandra", e);
        }

        if (row == null)
        {
            return null;
        }

        return new CassandraFile(fileUUID, row.getBytesUnsafe("file_content"));
    }

    @Override
    public void saveOrUpdate(final CassandraFile cassandraFile) throws FileStorageException
    {
        if (cassandraFile.getUuid() == null)
            cassandraFile.setUuid(CassandraUtil.generateUUID());

        try
        {
            CassandraUtil.saveOrUpdate(cassandraFile.getUuid(), cassandraFile.getContent());
        }
        catch (NoNodeAvailableException e)
        {
            throw new FileStorageException("[Cassandra] Cannot connect to Cassandra", e);
        }
    }
}
