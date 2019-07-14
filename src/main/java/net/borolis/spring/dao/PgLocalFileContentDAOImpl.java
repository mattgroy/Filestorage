package net.borolis.spring.dao;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.entity.LocalFile;
import net.borolis.spring.dao.interfaces.LocalFileContentDAO;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.LocalBDConnectionFailException;
import net.borolis.spring.exceptions.ResourceNotFoundException;

@Component
public class PgLocalFileContentDAOImpl extends AbstractPgDAOImpl<LocalFileContent> implements LocalFileContentDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PgLocalFileContentDAOImpl.class);

    @Autowired
    public PgLocalFileContentDAOImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Transactional
    @Override
    public LocalFileContent get(String hash)
    {
        LOGGER.info("[PostgreSQL] Finding a FileContent with hash: " + hash);
        LocalFileContent localFileContent = (LocalFileContent)sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFileContent.class)
                .add(Restrictions.eq("cassandraUUID", hash))
                .setMaxResults(1)
                .uniqueResult();
        if (localFileContent == null)
        {
            LOGGER.info("[PostgreSQL] Could not find FileContent with hash: " + hash);
            throw new ResourceNotFoundException("[PostgreSQL] Could not find FileContent with hash: " + hash);
        }

        LOGGER.info("[PostgreSQL] FileContent with hash: " + hash + " found");
        return localFileContent;
    }

    @Override
    public Collection<LocalFileContent> getFileContents() throws LocalBDConnectionFailException
    {
        LOGGER.info("[PostgreSQL] Getting all LocalFileContents");
        Collection<LocalFileContent> list = sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFileContent.class)
                .list();
        LOGGER.info("[PostgreSQL] " + list.size() + " LocalFileContents found");
        return list;
    }

    @Override
    public Collection<LocalFile> deleteByHash(String hash)
    {
        LocalFileContent localFileContent = get(hash);
        Collection<LocalFile> localFiles = localFileContent.getLocalFilesMeta();
        delete(localFileContent);
        return localFiles;
    }
}
