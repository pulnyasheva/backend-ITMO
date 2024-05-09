package com.academy.fintech.origination.core.service.application;

import lombok.Builder;

@Builder
public record Application(String id,
                          String clientId,
                          int requestedDisbursementAmount,
                          ApplicationStatus status) {
}
