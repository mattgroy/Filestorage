package net.borolis.spring.dao;

import java.util.UUID;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.FileStorageException;
import net.borolis.spring.dao.interfaces.FileContentDAO;
import net.borolis.spring.entity.LocalFileContent;

@Component
public class PgFileContentDAOImpl extends AbstractPgDAOImpl<LocalFileContent> implements FileContentDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PgFileContentDAOImpl.class);

    @Autowired
    public PgFileContentDAOImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Transactional
    @Override
    public LocalFileContent findByUUID(UUID uuid) throws FileStorageException
    {
        LOGGER.info("[PostgreSQL] Finding a FileContent with UUID: " + uuid);
        LocalFileContent localFileContent = (LocalFileContent)sessionFactory
                .getCurrentSession()
                .createCriteria(LocalFileContent.class)
                .add(Restrictions.eq("cassandraUUID", uuid))
                .setMaxResults(1)
                .uniqueResult();
        if (localFileContent == null)
        {
            throw new FileStorageException(
                    String.format("[PostgreSQL] Could not find FileContent with UUID: ", uuid));
        }

        LOGGER.info("[PostgreSQL] FileContent with UUID: " + uuid + " found");
        return localFileContent;
    }
}
