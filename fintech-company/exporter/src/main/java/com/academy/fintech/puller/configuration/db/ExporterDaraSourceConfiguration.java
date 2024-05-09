package com.academy.fintech.puller.configuration.db;

import com.academy.fintech.puller.core.service.export_task.db.entity.EntityExportTask;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.academy.fintech.puller.core.service.export_task.db",
        "com.academy.fintech.puller.core.service.dispatcher.db"},
        entityManagerFactoryRef = "exporterEntityManagerFactory")
public class ExporterDaraSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.exporter")
    public DataSource exporterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean exporterEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(exporterDataSource())
                .packages(EntityExportTask.class)
                .persistenceUnit("exporter")
                .build();
    }

}
