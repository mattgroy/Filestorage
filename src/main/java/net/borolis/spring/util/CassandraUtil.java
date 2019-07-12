package net.borolis.spring.util;

import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.uuid.Uuids;

/**
 * Работа с БД Apache Cassandra
 *
 * @author bliskov
 * @since July 5, 2019
 */
public class CassandraUtil
{
    private static CqlSession session;

    public static CqlSession getSession()
    {
        return (session == null) ? openSession() : session;
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

    /**
     * Close the session
     */
    public static void closeSession()
    {
        session.close();
        session = null;
    }

    /**
     * Open session to cassandra db
     *
     * @return CqlSession - session
     */
    public static CqlSession openSession()
    {
        return session = CqlSession.builder().build();
    }
}

