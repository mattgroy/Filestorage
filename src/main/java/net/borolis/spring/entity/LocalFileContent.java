package net.borolis.spring.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Сущность, описывающая контент файла в локальной БД
 *
 * @author mratkov
 * @since 12 июля, 2019
 */
// TODO: 12.07.19 На текущий момент файлы с одинаковым контентом считаются разными (т.к. присваиваются разные UUID)
@Entity
@Table(name = "tbl_files_content")
public class LocalFileContent implements LocalEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "cassandra_object_uuid")
    private UUID cassandraUUID;

    @Lob
    @Column(name = "content")
    private byte[] content;

    public LocalFileContent()
    {
    }

    public LocalFileContent(UUID cassandraUUID, byte[] content)
    {
        this.cassandraUUID = cassandraUUID;
        this.content = content;
    }

    public UUID getCassandraUUID()
    {
        return cassandraUUID;
    }

    public byte[] getContent()
    {
        return content;
    }

    public Long getId()
    {
        return id;
    }

    public void setCassandraUUID(UUID cassandraUUID)
    {
        this.cassandraUUID = cassandraUUID;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
