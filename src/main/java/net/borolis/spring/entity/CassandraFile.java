package net.borolis.spring.entity;

import java.nio.ByteBuffer;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

/**
 * Класс представления файла для Apache Cassandra
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Entity(defaultKeyspace = "file_manager_keyspace")
@CqlName("file_table")
public class CassandraFile
{
    @PartitionKey
    @CqlName("file_id")
    private UUID uuid;

    @CqlName("file_content")
    private ByteBuffer content;

    public CassandraFile()
    {
    }

    public CassandraFile(UUID uuid, ByteBuffer content)
    {
        this.uuid = uuid;
        this.content = content;
    }

    public ByteBuffer getContent()
    {
        return content;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setContent(ByteBuffer content)
    {
        this.content = content;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public String toString()
    {
        return getUuid() + "->" + getContent();
    }
}
