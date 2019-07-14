package net.borolis.spring.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Класс инициализации приложения
 *
 * @author mratkov
 * @since July 10, 2019
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
    @Override
    protected Class[] getRootConfigClasses()
    {
        return new Class[] { };
    }

    @Override
    protected Class[] getServletConfigClasses()
    {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings()
    {
        return new String[] { "/" };
    }
}
