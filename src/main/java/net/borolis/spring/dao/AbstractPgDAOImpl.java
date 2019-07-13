package net.borolis.spring.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.exceptions.FileStorageException;
import net.borolis.spring.dao.interfaces.DAO;
import net.borolis.spring.entity.LocalEntity;

/**
 * Абстрактный класс, в котором реализовано взаимодействие "по умолчанию" с объектами в PostgreSQL
 * @see DAO
 * @author mratkov
 * @since 9 июля, 2019
 */
public abstract class AbstractPgDAOImpl<T extends LocalEntity> implements DAO<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPgDAOImpl.class);

    final SessionFactory sessionFactory;

    AbstractPgDAOImpl(final SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void delete(T entity)
    {
        LOGGER.info("[PostgreSQL] Deleting " + entity);
        sessionFactory.getCurrentSession().delete(entity);
        LOGGER.info("[PostgreSQL] Entity " + entity + " deleted");
    }

    @Transactional
    @Override
    public void delete(long entityId)
    {
        delete(getById(entityId));
    }

    /**
     * Пояснения насчёт resolveTypeArgument(): он позволяет получить класс дженерика в рантайме (аля T.class)
     *
     * @throws FileStorageException Не удалось найти объект
     */
    @Transactional
    @Override
    public T getById(long id) throws FileStorageException
    {
        LOGGER.info("[PostgreSQL] Finding an entity with id " + id);
        Class<?> tClass = GenericTypeResolver.resolveTypeArgument(getClass(), AbstractPgDAOImpl.class);
        T entity = (T)sessionFactory
                .getCurrentSession()
                .createCriteria(tClass)
                .add(Restrictions.eq("id", id))
                .setMaxResults(1)
                .uniqueResult();
        if (entity == null)
        {
            throw new FileStorageException(
                    String.format("[PostgreSQL] Could not find \"%s\" with id \"%s\"", tClass, id));
        }

        LOGGER.info("[PostgreSQL] Entity " + entity + " found");
        return entity;
    }

    @Transactional
    @Override
    public void saveOrUpdate(T entity)
    {
        LOGGER.info("[PostgreSQL] Saving " + entity);
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        LOGGER.info("[PostgreSQL] Entity " + entity + " saved");
    }
}
