package net.borolis.spring.dao;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.dao.interfaces.LocalFileDAO;
import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.exceptions.LocalBDConnectionFailException;

/**
 * @see AbstractPgDAOImpl
 * @see LocalFileDAO
 * @author mratkov
 * @since 9 июля, 2019
 */
@Component
public class PgFileDAOImpl extends AbstractPgDAOImpl<LocalFile> implements LocalFileDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PgFileDAOImpl.class);

    @Autowired
    public PgFileDAOImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Transactional
    @Override
    public Collection<LocalFile> getFiles()
    {
        LOGGER.info("[PostgreSQL] Getting all LocalFiles");
        Collection<LocalFile> list = sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFile.class)
                .list();
        LOGGER.info("[PostgreSQL] " + list.size() + " LocalFiles found");
        return list;
    }

    @Transactional
    @Override
    public Collection<LocalFile> getFilesBy(String hash) throws LocalBDConnectionFailException
    {
        LOGGER.info("[PostgreSQL] Getting all LocalFiles with content hash " + hash);
        Collection<LocalFile> list = sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFile.class)
                .add(Restrictions.eq("hash", hash))
                .list();
        LOGGER.info("[PostgreSQL] " + list.size() + " LocalFiles with hash " + hash + " found");
        return list;
    }
}
