package com.academy.fintech.pg.core.origination.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origination.client.activate.grpc")
public record ActivationGrpcClientProperty(String host, int port) {
}
