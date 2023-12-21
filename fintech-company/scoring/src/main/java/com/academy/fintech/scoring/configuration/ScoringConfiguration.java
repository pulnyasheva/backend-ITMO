package com.academy.fintech.scoring.configuration;

import com.academy.fintech.scoring.core.pe.client.grpc.PEGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PEGrpcClientProperty.class})
public class ScoringConfiguration {
}

