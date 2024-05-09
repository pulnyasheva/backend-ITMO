package com.academy.fintech.pg.configuration;

import com.academy.fintech.pg.core.merchant_provider.client.rest.CheckDisbursementRestClientProperty;
import com.academy.fintech.pg.core.merchant_provider.client.rest.DisbursementRestClientProperty;
import com.academy.fintech.pg.core.origination.grpc.ActivationGrpcClientProperty;
import com.academy.fintech.pg.core.pe.client.grpc.ProcessingGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({ProcessingGrpcClientProperty.class,
        DisbursementRestClientProperty.class,
        CheckDisbursementRestClientProperty.class,
        ActivationGrpcClientProperty.class})
public class PGConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
