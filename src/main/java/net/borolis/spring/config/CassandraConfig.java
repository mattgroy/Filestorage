package net.borolis.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import net.borolis.spring.dao.interfaces.CassandraFileDAO;
import net.borolis.spring.dao.mappers.CassandraMapper;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Конфигурация бинов для работы с Cassandra
 * @author mratkov
 * @since 12 июля, 2019
 */
@Configuration
//@PropertySource(value = { "classpath:application.properties" })
public class CassandraConfig
{
    /**
     * Создание объекта сессии для выполнения CQL запросов в Cassandra
     * @return {@link CqlSession}
     */
    @Bean
    public CqlSession sqlSession()
    {
        CqlSession cqlSession = CqlSession.builder().build();
        return cqlSession;
    }

    /**
     * @param session инициализированная сессия {@link CqlSession}
     * @return Объект, создающий реализацию ДАО интерфейсов для переданной сессии
     */
    @Bean
    @Autowired
    public CassandraMapper cassandraMapper(CqlSession session)
    {
        return CassandraMapper.builder(session).build();
    }

    /**
     * Создание реализации {@link CassandraFileDAO}
     * @param cassandraMapper {@link CassandraMapper}
     * @return ДАО объект для работы с файлами в Кассандре
     */
    @Bean
    @Autowired
    public CassandraFileDAO cassandraFileDAO(CassandraMapper cassandraMapper)
    {
        return cassandraMapper.cassandraFileDAO();
    }
}
