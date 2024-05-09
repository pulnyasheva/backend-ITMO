package com.academy.fintech.puller.configuration.db;

import com.academy.fintech.puller.core.service.exporter.agreement.db.entity.EntityAgreement;
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
@EnableJpaRepositories(basePackages = {"com.academy.fintech.puller.core.service.exporter.agreement.db"},
        entityManagerFactoryRef = "agreementEntityManagerFactory")
public class AgreementDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.agreement")
    public DataSource agreementDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean agreementEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(agreementDataSource())
                .packages(EntityAgreement.class)
                .persistenceUnit("agreement")
                .build();
    }

}
