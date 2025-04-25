package com.example.library.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(DataSourceProperties.class)
public class JdbcConfig {

    private final DataSourceProperties properties;

    public JdbcConfig(DataSourceProperties properties) {
        this.properties = properties;
    }

    /**
     * Настраивает и возвращает пул подключений (DataSource) на основе свойств
     * spring.datasource.* из application.properties или application.yml
     */
    @Bean
    public DataSource dataSource() {
        return properties
                .initializeDataSourceBuilder()
                .build();
    }

    /**
     * JdbcTemplate для простых JDBC-запросов.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * NamedParameterJdbcTemplate для запросов с именованными параметрами.
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Менеджер транзакций на уровне DataSource.
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
