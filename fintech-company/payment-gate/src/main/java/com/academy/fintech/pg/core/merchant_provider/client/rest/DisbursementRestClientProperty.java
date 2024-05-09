package com.academy.fintech.pg.core.merchant_provider.client.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "merchant-provider.client.disbursement.rest")
public record DisbursementRestClientProperty(String url) {
}
