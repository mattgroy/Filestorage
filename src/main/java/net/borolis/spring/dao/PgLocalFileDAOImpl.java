package net.borolis.spring.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.dao.interfaces.LocalFileDAO;
import net.borolis.spring.entity.LocalFile;

/**
 * @see AbstractPgDAOImpl
 * @see LocalFileDAO
 * @author mratkov
 * @since 9 июля, 2019
 */
@Component
public class PgLocalFileDAOImpl extends AbstractPgDAOImpl<LocalFile> implements LocalFileDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PgLocalFileDAOImpl.class);

    @Autowired
    public PgLocalFileDAOImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Transactional
    @Override
    public List<LocalFile> getFiles()
    {
        LOGGER.info("[PostgreSQL] Getting all LocalFiles");
        return (List<LocalFile>)sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFile.class)
                .list();
    }
}
