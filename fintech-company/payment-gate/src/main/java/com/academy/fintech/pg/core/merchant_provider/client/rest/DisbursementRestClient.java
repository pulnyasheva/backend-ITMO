package com.academy.fintech.pg.core.merchant_provider.client.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class DisbursementRestClient {

    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public DisbursementRestClient(DisbursementRestClientProperty property, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        url = property.url();
    }

    public DisbursementResponse disburse(DisbursementRequest request) {
        ResponseEntity<DisbursementResponse> response = restTemplate.postForEntity(url, request, DisbursementResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Got error from merchant-provider issue payment by request: {}",
                    request.toString() + response.getStatusCode());
            throw new RuntimeException();
        }
    }
}
