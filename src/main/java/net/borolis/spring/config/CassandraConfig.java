package net.borolis.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import net.borolis.spring.util.CassandraDDL;

import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class CassandraConfig
{
    private final CassandraDDL cassandraDDL;
    private final String cassandraKeySpaceName;
    private final String cassandraTableName;

    @Autowired
    public CassandraConfig(final CassandraDDL cassandraDDL, final Environment environment)
    {
        this.cassandraDDL = cassandraDDL;
        this.cassandraKeySpaceName = environment.getRequiredProperty("cassandra.keyspace.name");
        this.cassandraTableName = environment.getRequiredProperty("cassandra.table.name");
    }

    @Bean
    public CqlSession sqlSession()
    {
        CqlSession cqlSession = CqlSession.builder().build();
        checkAndFixCassandraStructure(cqlSession);
        return cqlSession;
    }

    private void checkAndFixCassandraStructure(CqlSession cqlSession)
    {
        boolean isKeyspaceAndTableExists = cassandraDDL.isKeyspaceAndTableExists(
                cqlSession, cassandraKeySpaceName, cassandraTableName);
        if (isKeyspaceAndTableExists)
        {
            return;
        }

        boolean isKeyspaceExists = cassandraDDL.isKeyspaceExists(cqlSession, cassandraKeySpaceName);

        if (isKeyspaceExists)
        {
            cassandraDDL.createTable(cqlSession, cassandraKeySpaceName, cassandraTableName);
        }
        else
        {
            cassandraDDL.createKeyspace(cqlSession, cassandraKeySpaceName);
            cassandraDDL.createTable(cqlSession, cassandraKeySpaceName, cassandraTableName);
        }
    }
}
