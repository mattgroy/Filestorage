package net.borolis.spring.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.uuid.Uuids;

/**
 * Работа с БД Apache Cassandra
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Component
public class CassandraUtil
{
    private static CqlSession session;
    private static PreparedStatement deleteQuery;
    private static PreparedStatement getByUuidQuery;
    private static PreparedStatement saveOrUpdateQuery;

    @Autowired
    public CassandraUtil(final CqlSession session, final Environment environment)
    {
        CassandraUtil.session = session;
        String cassandraKeySpaceName = environment.getRequiredProperty("cassandra.keyspace.name");
        String cassandraTableName = environment.getRequiredProperty("cassandra.table.name");
        CassandraUtil.deleteQuery = session.prepare(
                "DELETE FROM " + cassandraKeySpaceName + "." + cassandraTableName + " WHERE " + "file_id=?;");
        CassandraUtil.getByUuidQuery = session.prepare(
                "SELECT * from  " + cassandraKeySpaceName + "." + cassandraTableName + " WHERE" + " file_id=?;");
        CassandraUtil.saveOrUpdateQuery = session.prepare(
                "INSERT INTO " + cassandraKeySpaceName + "." + cassandraTableName
                        + " (file_id, file_content) VALUES(?, ?)");
    }

    /**
     * Convenience-метод для подготовки и выполнения cql запроса
     * @param query подготовленный {@link PreparedStatement} запрос
     * @param bindObjects объекты, использующиеся в создании запроса
     * @return Результат выполнения запроса
     */
    private static ResultSet execute(PreparedStatement query, Object... bindObjects)
    {
        BoundStatement bound = query.bind(bindObjects);
        return session.execute(bound);
    }

    /**
     * Удаление с помощью подготовленного запроса
     * @param whereClause условие поиска объектов для удаления
     */
    public static void delete(Object whereClause)
    {
        CassandraUtil.execute(deleteQuery, whereClause);
    }

    /**
     * Поиск с помощью подготовленного запроса
     * @param uuid uuid искомого объекта
     * @return результат выполнения запроса
     */
    public static ResultSet getByUUID(UUID uuid)
    {
        return CassandraUtil.execute(getByUuidQuery, uuid);
    }

    /**
     * Сохранение или обновление с помощью подготовленного запроса
     * @param bindings поля объекта, который будет сохранён
     */
    public static void saveOrUpdate(Object... bindings)
    {
        CassandraUtil.execute(saveOrUpdateQuery, bindings);
    }

    /**
     * Generate time-based UUID
     *
     * @return UUID
     */
    public static UUID generateUUID()
    {
        return Uuids.timeBased();
    }
}
