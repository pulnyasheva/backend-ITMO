package com.academy.fintech.origination.core.pg.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment-gate.client.disbursement.grpc")
public record DisbursementGrpcClientProperty(String host, int port) {
}
