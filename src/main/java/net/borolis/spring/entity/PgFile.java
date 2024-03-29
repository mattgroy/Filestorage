package net.borolis.spring.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Сущность описывающая представление файла
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Entity
@Table(name = "tbl_files")
public class PgFile
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String mimeType;

    @Column(name = "cassandra_object_uuid")
    private UUID cassandraObjectId;

    public PgFile()
    {

    }

    public PgFile(long id)
    {
        this.id = id;
    }

    public PgFile(String title, String mimeType, UUID cassandraObjectId)
    {
        this.title = title;
        this.cassandraObjectId = cassandraObjectId;
        this.mimeType = mimeType;
    }

    public UUID getCassandraObjectId()
    {
        return cassandraObjectId;
    }

    public Long getId()
    {
        return id;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public String getTitle()
    {
        return title;
    }

    public void setCassandraObjectId(UUID cassandraObjectId)
    {
        this.cassandraObjectId = cassandraObjectId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return getId() + "  " + getTitle() + "  " + getCassandraObjectId() + "  " + getMimeType();
    }
}
