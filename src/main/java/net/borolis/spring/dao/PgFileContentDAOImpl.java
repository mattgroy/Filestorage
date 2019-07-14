package net.borolis.spring.dao;

import java.util.Collection;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.dao.interfaces.LocalFileContentDAO;
import net.borolis.spring.entity.LocalFileContent;
import net.borolis.spring.exceptions.LocalBDConnectionFailException;

@Component
public class PgFileContentDAOImpl extends AbstractPgDAOImpl<LocalFileContent> implements LocalFileContentDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PgFileContentDAOImpl.class);

    @Autowired
    public PgFileContentDAOImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Transactional
    @Override
    public Optional<LocalFileContent> getBy(String hash)
    {
        LOGGER.info("[PostgreSQL] Finding a FileContent with hash: " + hash);
        LocalFileContent localFileContent = (LocalFileContent)sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFileContent.class)
                .add(Restrictions.eq("hash", hash))
                .setMaxResults(1)
                .uniqueResult();
        if (localFileContent == null)
        {
            LOGGER.info("[PostgreSQL] Could not find FileContent with hash: " + hash);
            return Optional.empty();
            //            throw new ResourceNotFoundException("[PostgreSQL] Could not find FileContent with hash: " +
            //            hash);
        }

        LOGGER.info("[PostgreSQL] FileContent with hash: " + hash + " found");
        return Optional.of(localFileContent);
    }

    @Transactional
    @Override
    public Collection<LocalFileContent> getFilesContent() throws LocalBDConnectionFailException
    {
        LOGGER.info("[PostgreSQL] Getting all LocalFileContents");
        Collection<LocalFileContent> list = sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFileContent.class)
                .list();
        LOGGER.info("[PostgreSQL] " + list.size() + " LocalFileContents found");
        return list;
    }

    @Transactional
    @Override
    public void deleteBy(String hash)
    {
        getBy(hash).ifPresent(this::delete);
        //        delete(getBy(hash));
    }
}
