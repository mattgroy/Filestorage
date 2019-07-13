package net.borolis.spring.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.dao.interfaces.FileMetaDAO;
import net.borolis.spring.entity.LocalFile;

/**
 * @see AbstractPgDAOImpl
 * @see FileMetaDAO
 * @author mratkov
 * @since 9 июля, 2019
 */
@Component
public class PgFileMetaDAOImpl extends AbstractPgDAOImpl<LocalFile> implements FileMetaDAO
{
    @Autowired
    public PgFileMetaDAOImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Transactional
    @Override
    public List<LocalFile> getFiles()
    {
        return (List<LocalFile>)sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFile.class)
                .list();
    }
}
