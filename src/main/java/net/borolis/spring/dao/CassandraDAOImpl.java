package net.borolis.spring.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import net.borolis.spring.entity.CassandraFile;
import net.borolis.spring.util.CassandraUtil;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

/**
 * @see net.borolis.spring.dao.CassandraDAO
 * @author bliskov
 * @since July 5, 2019
 */
@Component
public class CassandraDAOImpl implements CassandraDAO
{
    private final String cassandraKeySpaceName;
    private final String cassandraTableName;

    @Autowired
    public CassandraDAOImpl(final Environment environment)
    {
        this.cassandraKeySpaceName = environment.getRequiredProperty("cassandra.keyspace.name");
        this.cassandraTableName = environment.getRequiredProperty("cassandra.table.name");
    }

    @Override
    public void delete(final CassandraFile cassandraFile)
    {
        String query = "DELETE FROM " + cassandraKeySpaceName + "." + cassandraTableName + " WHERE " + "file_id=?;";
        PreparedStatement prepared = CassandraUtil.getSession().prepare(query);
        BoundStatement bound = prepared.bind(cassandraFile.getUuid());
        CassandraUtil.getSession().execute(bound);
    }

    @Override
    public void delete(final UUID fileUUID)
    {
        Optional<CassandraFile> fileOptional = getByUUID(fileUUID);
        fileOptional.ifPresent(this::delete);
    }

    @Override
    public Optional<CassandraFile> getByUUID(final UUID fileUUID)
    {
        String query = "SELECT * from  " + cassandraKeySpaceName + "." + cassandraTableName + " WHERE" + " file_id=?;";
        PreparedStatement prepared = CassandraUtil.getSession().prepare(query);
        BoundStatement bound = prepared.bind(fileUUID);

        Row row = CassandraUtil.getSession().execute(bound).one();

        if (row == null)
        {
            return Optional.empty();
        }

        return Optional.of(new CassandraFile(fileUUID, row.getBytesUnsafe("file_content")));
    }

    @Override
    public UUID saveOrUpdate(final CassandraFile cassandraFile)
    {
        cassandraFile.setUuid(CassandraUtil.generateUUID());

        String query = "INSERT INTO " + cassandraKeySpaceName + "." + cassandraTableName + " (file_id, file_content) "
                + "VALUES(?, ?)";
        PreparedStatement prepared = CassandraUtil.getSession().prepare(query);

        BoundStatement bound = prepared.bind(cassandraFile.getUuid(), cassandraFile.getContent());
        CassandraUtil.getSession().execute(bound);
        return cassandraFile.getUuid();
    }
}
