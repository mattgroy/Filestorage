package net.borolis.spring.util;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Работа со структурой БД Apache Cassandra
 *
 * @author bliskov
 * @since July 8, 2019
 */
@Component
public class CassandraDDL
{
    /**
     * Проверка на существование keyspace и table
     *
     * @param keyspaceName keyspace name
     * @param tableName table name
     * @return наличие keyspace  & table
     */
    public boolean isKeyspaceAndTableExists(final String keyspaceName, final String tableName)
    {
        String query = "SELECT * FROM system_schema.tables WHERE keyspace_name=? and table_name=?";
        PreparedStatement prepared = CassandraUtil.getSession().prepare(query);
        BoundStatement bound = prepared.bind(keyspaceName, tableName);
        Row row = CassandraUtil.getSession().execute(bound).one();
        return row != null;
    }

    /**
     * Проверка на существование keyspace
     *
     * @param keyspaceName keyspace name
     * @return наличие keyspace
     */
    public boolean isKeyspaceExists(final String keyspaceName)
    {
        String query = "select * from system_schema.keyspaces where keyspace_name=?;";
        PreparedStatement prepared = CassandraUtil.getSession().prepare(query);
        BoundStatement bound = prepared.bind(keyspaceName);
        Row row = CassandraUtil.getSession().execute(bound).one();
        return row != null;
    }

    /**
     * Создание нового keyspace
     *
     * @param keyspaceName keyspace name
     */
    public void createKeyspace(final String keyspaceName)
    {
        String query = "CREATE KEYSPACE " + keyspaceName + " WITH durable_writes = true AND replication="
                + "{'class':'SimpleStrategy','replication_factor':2};";
        System.out.println("borolis: create keyspace:" + query);
        SimpleStatement simpleStatement = SimpleStatement.newInstance(query).setTimeout(Duration.ofSeconds(10L));
        CassandraUtil.getSession().execute(simpleStatement);
    }

    /**
     * Создание новой table в keyspace
     *
     * @param keyspaceName keyspace name
     * @param tableName table name
     */
    public void createTable(final String keyspaceName, final String tableName)
    {
        String query =
                "CREATE TABLE " + keyspaceName + "." + tableName
                        + " (file_id uuid, file_content blob, PRIMARY KEY (file_id)"
                        + ");";
        System.out.println("borolis: create table:" + query);
        SimpleStatement simpleStatement = SimpleStatement.newInstance(query).setTimeout(Duration.ofSeconds(10L));
        CassandraUtil.getSession().execute(simpleStatement);
    }

    /**
     * Удаление keyspace
     *
     * @param keyspaceName keyspace name
     */
    public void dropKeySpace(final String keyspaceName)
    {
        String query =
                "DROP KEYSPACE IF EXISTS " + keyspaceName + ";";
        System.out.println("borolis: drop keyspace if exists:" + query);
        SimpleStatement simpleStatement = SimpleStatement.newInstance(query).setTimeout(Duration.ofSeconds(10L));
        CassandraUtil.getSession().execute(simpleStatement);
    }
}
