package net.borolis.spring.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.borolis.spring.entity.PgFile;
import net.borolis.spring.util.HibernateUtil;

/**
 * @see net.borolis.spring.dao.PgFileDAO
 * @author bliskov
 * @since July 5, 2019
 */
@Component
public class PgFileDAOImpl implements PgFileDAO
{
    private final HibernateUtil hibernateUtil;

    @Autowired
    public PgFileDAOImpl(final HibernateUtil hibernateUtil)
    {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void delete(final PgFile pgFile)
    {
        final Session pgSession = hibernateUtil.getSessionFactory().openSession();

        try
        {
            pgSession.beginTransaction();
            pgSession.delete(pgFile);
            pgSession.getTransaction().commit();
        }
        catch (Exception e)
        {
            pgSession.getTransaction().rollback();
            e.printStackTrace();
        }
        finally
        {
            pgSession.close();
        }
    }

    @Override
    public void delete(final long fileId)
    {
        Optional<PgFile> fileOptional = getById(fileId);
        fileOptional.ifPresent(this::delete);
    }

    @Override
    public Optional<PgFile> getById(final long id)
    {
        final Session pgSession = hibernateUtil.getSessionFactory().openSession();

        try
        {
            pgSession.beginTransaction();

            final PgFile file = (PgFile)pgSession.createCriteria(PgFile.class)
                    .add(Restrictions.eq("id", id))
                    .setMaxResults(1)
                    .uniqueResult();
            pgSession.getTransaction().commit();

            return Optional.ofNullable(file);
        }
        catch (Exception e)
        {
            pgSession.getTransaction().rollback();
            e.printStackTrace();
            return Optional.empty();
        }
        finally
        {
            pgSession.close();
        }
    }

    @Override
    public List<PgFile> getFiles()
    {
        final Session pgSession = hibernateUtil.getSessionFactory().openSession();

        List<PgFile> files;
        try
        {
            pgSession.beginTransaction();
            files = (List<PgFile>)pgSession.createCriteria(PgFile.class).list();
            pgSession.getTransaction().commit();
        }
        catch (Exception e)
        {
            files = new ArrayList<>();
            pgSession.getTransaction().rollback();
            e.printStackTrace();
        }
        finally
        {
            pgSession.close();
        }

        return files;
    }

    @Override
    public void saveOrUpdate(final PgFile pgFile)
    {
        final Session pgSession = hibernateUtil.getSessionFactory().openSession();

        try
        {
            pgSession.beginTransaction();
            pgSession.saveOrUpdate(pgFile);
            pgSession.getTransaction().commit();
        }
        catch (Exception e)
        {
            pgSession.getTransaction().rollback();
            e.printStackTrace();
        }
        finally
        {
            pgSession.close();
        }
    }
}
