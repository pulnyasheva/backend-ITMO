package com.academy.fintech.origination.core.exporter.client.export.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exporter.client.export.grpc")
public record ExportGrpcClientProperty(String host, int port) {
}