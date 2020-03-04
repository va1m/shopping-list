package com.github.va1m.shopping.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Database connection pool configurator
 */
@Configuration
public class DataSourceConfig {

    /**
     * Reads database settings from the application.properties file
     */
    @Bean
    @ConfigurationProperties("app.datasource")
    public HikariDataSource dataSource() {
        return (HikariDataSource) DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
