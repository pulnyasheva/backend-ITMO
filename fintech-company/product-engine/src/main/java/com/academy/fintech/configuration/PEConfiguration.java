package com.academy.fintech.configuration;

import com.academy.fintech.pe.exporter.client.export.grpc.ExportGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ExportGrpcClientProperty.class})
public class PEConfiguration {
}
