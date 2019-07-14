package net.borolis.spring.dao.mappers;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;

import net.borolis.spring.entity.CassandraFile;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.select.Select;

public class CassandraFileProvider
{
    private final CqlSession session;
    private final PreparedStatement preparedStatement;

    public CassandraFileProvider(MapperContext context, EntityHelper<CassandraFile> cassandraFileHelper)
    {
        this.session = context.getSession();
        Select select = cassandraFileHelper.selectStart().whereColumn("file_hash").isEqualTo(bindMarker());
        this.preparedStatement = session.prepare(select.build());
    }

    public boolean isHashStored(String hash)
    {
        BoundStatementBuilder boundStatementBuilder =
                preparedStatement.boundStatementBuilder().setString("hash", hash);

        return session.execute(boundStatementBuilder.build()).one() != null;
    }
}
