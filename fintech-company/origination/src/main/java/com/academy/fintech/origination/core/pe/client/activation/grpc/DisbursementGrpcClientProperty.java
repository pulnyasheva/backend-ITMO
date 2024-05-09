package com.academy.fintech.origination.core.pe.client.activation.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "product-engine.client.disbursement.grpc")
public record DisbursementGrpcClientProperty(String host, int port) {
}
