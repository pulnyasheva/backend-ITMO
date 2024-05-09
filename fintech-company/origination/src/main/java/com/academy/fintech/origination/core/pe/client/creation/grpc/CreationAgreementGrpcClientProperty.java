package com.academy.fintech.origination.core.pe.client.creation.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "product-engine.client.creation.grpc")
public record CreationAgreementGrpcClientProperty(String host, int port) {
}
