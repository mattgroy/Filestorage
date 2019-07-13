package net.borolis.spring.util;

import java.util.UUID;

import com.datastax.oss.driver.api.core.uuid.Uuids;

/**
 * Работа с БД Apache Cassandra
 *
 * @author bliskov
 * @author mratkov
 * @since July 5, 2019
 */

public class CassandraUtil
{
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
