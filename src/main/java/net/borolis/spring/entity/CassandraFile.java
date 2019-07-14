package net.borolis.spring.entity;

import java.nio.ByteBuffer;

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
//@Entity
@CqlName("file_table")
public class CassandraFile
{
    @PartitionKey
    @CqlName("file_hash")
    private String hash;

    @CqlName("file_content")
    private ByteBuffer content;

    public CassandraFile()
    {
    }

    public CassandraFile(String hash, ByteBuffer content)
    {
        this.hash = hash;
        this.content = content;
    }

    public ByteBuffer getContent()
    {
        return content;
    }

    public String getHash()
    {
        return hash;
    }

    public void setContent(ByteBuffer content)
    {
        this.content = content;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    @Override
    public String toString()
    {
        return getHash() + "->" + getContent();
    }
}
