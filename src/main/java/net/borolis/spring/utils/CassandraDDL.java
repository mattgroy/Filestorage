package net.borolis.spring.utils;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Работа со структурой БД Apache Cassandra
 *
 * @author bliskov
 * @author mratkov
 * @since July 8, 2019
 */
@Component
public class CassandraDDL
{
    public static void checkAndFixCassandraStructure(
            CqlSession cqlSession,
            String cassandraKeySpaceName,
            String cassandraTableName)
    {
        boolean isKeyspaceAndTableExists = isKeyspaceAndTableExists(
                cqlSession,
                cassandraKeySpaceName,
                cassandraTableName);
        if (isKeyspaceAndTableExists)
        {
            return;
        }

        boolean isKeyspaceExists = isKeyspaceExists(cqlSession, cassandraKeySpaceName);

        if (isKeyspaceExists)
        {
            createTable(cqlSession, cassandraKeySpaceName, cassandraTableName);
        }
        else
        {
            createKeyspace(cqlSession, cassandraKeySpaceName);
            createTable(cqlSession, cassandraKeySpaceName, cassandraTableName);
        }
    }

    /**
     * Создание нового keyspace
     *
     * @param keyspaceName keyspace name
     */
    private static void createKeyspace(final CqlSession cqlSession, final String keyspaceName)
    {
        String query = "CREATE KEYSPACE " + keyspaceName + " WITH durable_writes = true AND replication="
                + "{'class':'SimpleStrategy','replication_factor':2};";
        System.out.println("borolis: create keyspace:" + query);
        SimpleStatement simpleStatement = SimpleStatement.newInstance(query).setTimeout(Duration.ofSeconds(10L));
        cqlSession.execute(simpleStatement);
    }

    /**
     * Создание новой table в keyspace
     *
     * @param keyspaceName keyspace name
     * @param tableName table name
     */
    private static void createTable(final CqlSession cqlSession, final String keyspaceName, final String tableName)
    {
        String query =
                "CREATE TABLE " + keyspaceName + "." + tableName
                        + " (file_id uuid, file_content blob, PRIMARY KEY (file_id)"
                        + ");";
        System.out.println("borolis: create table:" + query);
        SimpleStatement simpleStatement = SimpleStatement.newInstance(query).setTimeout(Duration.ofSeconds(10L));
        cqlSession.execute(simpleStatement);
    }

    /**
     * Удаление keyspace
     *
     * @param keyspaceName keyspace name
     */
    private static void dropKeySpace(final CqlSession cqlSession, final String keyspaceName)
    {
        String query =
                "DROP KEYSPACE IF EXISTS " + keyspaceName + ";";
        System.out.println("borolis: drop keyspace if exists:" + query);
        SimpleStatement simpleStatement = SimpleStatement.newInstance(query).setTimeout(Duration.ofSeconds(10L));
        cqlSession.execute(simpleStatement);
    }

    /**
     * Проверка на существование keyspace и table
     *
     * @param keyspaceName keyspace name
     * @param tableName table name
     * @return наличие keyspace  & table
     */
    private static boolean isKeyspaceAndTableExists(final CqlSession cqlSession, final String keyspaceName, final String tableName)
    {
        String query = "SELECT * FROM system_schema.tables WHERE keyspace_name=? and table_name=?";
        PreparedStatement prepared = cqlSession.prepare(query);
        BoundStatement bound = prepared.bind(keyspaceName, tableName);
        Row row = cqlSession.execute(bound).one();
        return row != null;
    }

    /**
     * Проверка на существование keyspace
     *
     * @param keyspaceName keyspace name
     * @return наличие keyspace
     */
    private static boolean isKeyspaceExists(final CqlSession cqlSession, final String keyspaceName)
    {
        String query = "select * from system_schema.keyspaces where keyspace_name=?;";
        PreparedStatement prepared = cqlSession.prepare(query);
        BoundStatement bound = prepared.bind(keyspaceName);
        Row row = cqlSession.execute(bound).one();
        return row != null;
    }
}
