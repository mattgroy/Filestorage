package net.borolis.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Класс конфигурации приложения
 *
 * @author bliskov
 * @author mratkov
 * @since July 10, 2019
 */
@Configuration
@PropertySource({ "classpath:application.properties" })
public class AppConfig
{
    private final int maxUploadSize;

    @Autowired
    public AppConfig(final Environment environment)
    {
        this.maxUploadSize = environment.getProperty("upload.file.size.max", Integer.class);
    }

    @Bean
    public CommonsMultipartResolver multipartResolver()
    {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(maxUploadSize);
        cmr.setMaxUploadSizePerFile(maxUploadSize); //bytes
        return cmr;
    }
}