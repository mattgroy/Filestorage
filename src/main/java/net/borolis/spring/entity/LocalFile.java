package net.borolis.spring.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Сущность описывающая представление файла в локальной БД
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Entity
@Table(name = "tbl_files")
public class LocalFile implements LocalEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * Название файла
     */
    @Column(name = "title")
    private String title;

    /**
     * MIME-тип файла
     */
    @Column(name = "type")
    private String mimeType;

    /**
     * UUID файла
     */
    @Column(name = "cassandra_object_uuid")
    private UUID cassandraObjectId;

    /**
     * Контент файла
     */
    @Lob
    @Column(name = "content")
    private byte[] content;

    public LocalFile()
    {
    }

    public LocalFile(String title, String mimeType, UUID cassandraObjectId, byte[] content)
    {
        this.title = title;
        this.cassandraObjectId = cassandraObjectId;
        this.mimeType = mimeType;
        this.content = content;
    }

    public UUID getCassandraObjectId()
    {
        return cassandraObjectId;
    }

    @JsonIgnore
    public byte[] getContent()
    {
        return content;
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

    @JsonProperty
    public void setContent(byte[] content)
    {
        this.content = content;
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
