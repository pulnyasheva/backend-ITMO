package com.academy.fintech.pg.core.merchant_provider.client.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CheckDisbursementRestClient {

    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public CheckDisbursementRestClient(CheckDisbursementRestClientProperty property, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        url = property.url();
    }

    public CheckDisbursementResponse checkDisbursement(CheckDisbursementRequest request) {
        ResponseEntity<CheckDisbursementResponse> response = restTemplate.postForEntity(url, request, CheckDisbursementResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Got error from merchant-provider ready issue payment by request: {}",
                    request.toString() + response.getStatusCode());
            throw new RuntimeException();
        }
    }
}
