package com.academy.fintech.pg.core.merchant_provider.client.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "merchant-provider.client.check-disbursement.rest")
public record CheckDisbursementRestClientProperty(String url) {
}
