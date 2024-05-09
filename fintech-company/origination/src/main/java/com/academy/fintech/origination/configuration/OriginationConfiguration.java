package com.academy.fintech.origination.configuration;

import com.academy.fintech.origination.core.exporter.client.export.grpc.ExportGrpcClientProperty;
import com.academy.fintech.origination.core.pe.client.activation.grpc.DisbursementGrpcClientProperty;
import com.academy.fintech.origination.core.pe.client.creation.grpc.CreationAgreementGrpcClientProperty;
import com.academy.fintech.origination.core.scoring.client.grpc.ScoringGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ScoringGrpcClientProperty.class,
        CreationAgreementGrpcClientProperty.class,
        com.academy.fintech.origination.core.pg.client.grpc.DisbursementGrpcClientProperty.class,
        DisbursementGrpcClientProperty.class,
        ExportGrpcClientProperty.class})
public class OriginationConfiguration {
}
