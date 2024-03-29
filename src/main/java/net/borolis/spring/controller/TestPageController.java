package net.borolis.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import net.borolis.spring.util.CassandraDDL;
import net.borolis.spring.util.CassandraUtil;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

/**
 * Контроллер для тестовых запросов
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Controller
public class TestPageController
{
    private final CassandraDDL cassandraDDL;

    @Autowired
    public TestPageController(final CassandraDDL cassandraDDL)
    {
        this.cassandraDDL = cassandraDDL;
    }

    /**
     * Проверка на существование keyspace в БД
     *
     * @param keyspaceName название keyspace
     * @return наличие keyspace в БД
     */
    @GetMapping("/test/is_keyspace_exist/{tbl}")
    @ResponseBody
    public boolean isKeyspaceExist(@PathVariable("tbl") final String keyspaceName)
    {
        String query = "SELECT * FROM system_schema.keyspaces WHERE keyspace_name=?;";
        PreparedStatement prepared = CassandraUtil.getSession().prepare(query);
        BoundStatement bound = prepared.bind(keyspaceName);
        Row row = CassandraUtil.getSession().execute(bound).one();
        return row != null;
    }

    /**
     * Создание keyspace в БД
     *
     * @param keyspaceName название keyspace
     * @return trash
     */
    @GetMapping("/test/create_keyspace/{keyspace_name}")
    @ResponseBody
    public String createKeyspace(@PathVariable("keyspace_name") final String keyspaceName)
    {
        try
        {
            cassandraDDL.createKeyspace(keyspaceName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.toString();
        }
        return "OK";
    }

}