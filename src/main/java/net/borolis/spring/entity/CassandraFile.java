package net.borolis.spring.entity;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Класс представления файла для Apache Casssandra
 *
 * @author bliskov
 * @since July 5, 2019
 */
public class CassandraFile
{
    private UUID uuid;
    private ByteBuffer content;

    public CassandraFile()
    {

    }

    public CassandraFile(ByteBuffer content)
    {
        this.content = content;
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
