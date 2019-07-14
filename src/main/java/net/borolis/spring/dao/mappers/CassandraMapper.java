package net.borolis.spring.dao.mappers;

import net.borolis.spring.dao.interfaces.CassandraFileDAO;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperBuilder;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * Класс, возвращающий реализаций DAO для Entity-объектов
 * ({@link com.datastax.oss.driver.api.mapper.annotations.Entity}) Кассандры
 * @author mratkov
 * @since 12 июля, 2019
 */
@Mapper
public interface CassandraMapper
{
    /**
     * Convenience-метод, создающий реализацию {@link CassandraMapper},
     * скрывая от внешенего мира генерируемый в момент компиляции класс CassandraMapperBuilder
     * @param session {@link CqlSession}
     * @return {@link MapperBuilder}
     */
    static MapperBuilder<CassandraMapper> builder(CqlSession session)
    {
        return new CassandraMapperBuilder(session);
    }

    /**
     * Реализация интерфейса {@link CassandraFileDAO} с помощью {@link DaoFactory}
     * @return сгенерированная маппером {@link CassandraMapper} реализация интерфейса {@link CassandraFileDAO}
     */
    @DaoFactory
    CassandraFileDAO cassandraFileDAO();

    @DaoFactory
    CassandraFileDAO cassandraFileDAO(@DaoTable String table);
}
