package net.borolis.spring.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Сущность, описывающая контент файла в локальной БД
 *
 * @author mratkov
 * @since 12 июля, 2019
 */
@Entity
@Table(name = "tbl_files_content")
public class LocalFileContent implements LocalEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * Хэш контента
     */
    @Column(name = "hash")
    private String hash;

    /**
     * Контент файла
     */
    @Lob
    @Column(name = "content")
//    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    public LocalFileContent()
    {
    }

    public LocalFileContent(String hash, byte[] content)
    {
        this.hash = hash;
        this.content = content;
    }

    public String getHash()
    {
        return hash;
    }

    public byte[] getContent()
    {
        return content;
    }

    public Long getId()
    {
        return id;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
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
