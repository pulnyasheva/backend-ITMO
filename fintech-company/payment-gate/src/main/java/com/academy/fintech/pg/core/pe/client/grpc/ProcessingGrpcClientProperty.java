package com.academy.fintech.pg.core.pe.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "product-engine.client.processing.grpc")
public record ProcessingGrpcClientProperty(String host, int port) {
}