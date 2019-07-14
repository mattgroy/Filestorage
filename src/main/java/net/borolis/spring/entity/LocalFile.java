package net.borolis.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    //    @Basic(fetch = FetchType.LAZY)
    private String title;

    /**
     * MIME-тип файла
     */
    @Column(name = "type")
    //    @Basic(fetch = FetchType.LAZY)
    private String mimeType;

    /**
     * Хэш контента
     */
    @JsonIgnore
    @Column(name = "hash")
    //    @Basic(fetch = FetchType.LAZY)
    private String hash;

    public LocalFile()
    {
    }

    public LocalFile(String title, String mimeType, String hash)
    {
        this.title = title;
        this.hash = hash;
        this.mimeType = mimeType;
    }

    public String getHash()
    {
        return hash;
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

    public void setHash(String hash)
    {
        this.hash = hash;
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
        return getId() + "  " + getTitle() + "  " + getHash() + "  " + getMimeType();
    }

    //    @Override
    //    public boolean equals(Object o)
    //    {
    //        if (this == o)
    //            return true;
    //        if (o == null || getClass() != o.getClass())
    //            return false;
    //        LocalFile localFile = (LocalFile)o;
    //        return getId().equals(localFile.getId());
    //    }
    //
    //    @Override
    //    public int hashCode()
    //    {
    //        return Objects.hash(getId());
    //    }
}
