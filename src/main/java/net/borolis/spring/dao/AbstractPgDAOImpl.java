package net.borolis.spring.dao;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import net.borolis.spring.dao.interfaces.LocalDAO;
import net.borolis.spring.entity.LocalEntity;

/**
 * Абстрактный класс, в котором реализовано взаимодействие "по умолчанию" с объектами в PostgreSQL
 * @see LocalDAO
 * @author mratkov
 * @since 9 июля, 2019
 */
public abstract class AbstractPgDAOImpl<T extends LocalEntity> implements LocalDAO<T>
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

//    @Transactional
//    @Override
//    public void deleteBy(long entityId)
//    {
//        getBy(entityId).ifPresent(this::delete);
//    }

    /**
     * Пояснения насчёт resolveTypeArgument(): он позволяет получить класс дженерика в рантайме (аля T.class)
     */
    @Transactional
    @Override
    public Optional<T> getBy(long id)
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
            LOGGER.info(String.format("[PostgreSQL] Could not find \"%s\" with id \"%s\"", tClass.getName(), id));
            return Optional.empty();
//            throw new ResourceNotFoundException(
//                    String.format("[PostgreSQL] Could not find \"%s\" with id \"%s\"", tClass.getName(), id));
        }
        LOGGER.info("[PostgreSQL] Entity " + entity + " found");
        return Optional.of(entity);
    }

    @Transactional
    @Override
    public void save(T entity)
    {
        LOGGER.info("[PostgreSQL] Saving " + entity);
        sessionFactory.getCurrentSession().save(entity);
        LOGGER.info("[PostgreSQL] Entity " + entity + " saved");
    }
}
