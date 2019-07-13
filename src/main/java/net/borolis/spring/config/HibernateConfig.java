package net.borolis.spring.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Класс конфигурации Hibernate
 * @author mratkov
 * @since 10 июля, 2019
 */
@Configuration
@PropertySource({ "classpath:hibernate.properties" })
@EnableTransactionManagement
public class HibernateConfig
{
    private final Environment env;

    @Autowired
    public HibernateConfig(final Environment env)
    {
        this.env = env;
    }

    /**
     * Настройка подключения к БД с пулом из dbcp
     */
    @Bean
    public DataSource dataSource()
    {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("connection.driver_class"));
        dataSource.setUrl(env.getProperty("connection.url"));
        dataSource.setUsername(env.getProperty("connection.username"));
        dataSource.setPassword(env.getProperty("connection.password"));
        return dataSource;
    }

    /**
     * @return Сущность, создающая подключения к БД (Постгресс)
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory()
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan("net.borolis.spring.entity");
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }

    /**
     * @return Менеджер транзакций (из коробки)
     */
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    /**
     * Создание объекта с настройками Hibernate
     * @return Объект со свойствами Hibernate
     */
    private Properties getHibernateProperties()
    {
        return new Properties()
        {
            {
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hbm2ddl.auto"));
                setProperty("hibernate.dialect", env.getProperty("dialect"));
                setProperty("show_sql", env.getProperty("show_sql"));
            }
        };
    }
}
