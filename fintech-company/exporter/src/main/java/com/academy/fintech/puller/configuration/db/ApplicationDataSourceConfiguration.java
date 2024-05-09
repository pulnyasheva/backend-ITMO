package com.academy.fintech.puller.configuration.db;

import com.academy.fintech.puller.core.service.exporter.application.db.entity.EntityApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.academy.fintech.puller.core.service.exporter.application.db"},
        entityManagerFactoryRef = "applicationEntityManagerFactory")
public class ApplicationDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.application")
    public DataSource applicationDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(applicationDataSource())
                .packages(EntityApplication.class)
                .persistenceUnit("application")
                .build();
    }
}
