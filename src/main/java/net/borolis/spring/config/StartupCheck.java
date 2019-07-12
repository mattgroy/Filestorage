package net.borolis.spring.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import net.borolis.spring.util.CassandraDDL;

/**
 * Предстартовые проверки
 *
 * @author bliskov
 * @since July 8, 2019
 */
@Controller
@PropertySource(value = { "classpath:application.properties" })
public class StartupCheck
{
    private final CassandraDDL cassandraDDL;

    private final String cassandraKeySpaceName;
    private final String cassandraTableName;

    @Autowired
    public StartupCheck(final CassandraDDL cassandraDDL, final Environment environment)
    {
        this.cassandraDDL = cassandraDDL;
        this.cassandraKeySpaceName = environment.getRequiredProperty("cassandra.keyspace.name");
        this.cassandraTableName = environment.getRequiredProperty("cassandra.table.name");
    }

    /**
     * Проверка корректности структуры БД Cassandra
     * Исправление структуры в случае некорректности
     */
    @PostConstruct
    private void checkAndFixCassandraStructure()
    {
        boolean isKeyspaceAndTableExists = cassandraDDL.isKeyspaceAndTableExists(cassandraKeySpaceName,
                cassandraTableName);
        if (isKeyspaceAndTableExists)
        {
            return;
        }

        boolean isKeyspaceExists = cassandraDDL.isKeyspaceExists(cassandraKeySpaceName);

        if (isKeyspaceExists)
        {
            cassandraDDL.createTable(cassandraKeySpaceName, cassandraTableName);
        }
        else
        {
            cassandraDDL.createKeyspace(cassandraKeySpaceName);
            cassandraDDL.createTable(cassandraKeySpaceName, cassandraTableName);
        }
    }
}
