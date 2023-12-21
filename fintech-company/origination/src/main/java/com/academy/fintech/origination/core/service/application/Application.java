package com.academy.fintech.origination.core.service.application;

import lombok.Builder;
import lombok.Setter;

@Builder
@Setter
public record Application(String id,
                          String clientId,
                          int requestedDisbursementAmount,
                          ApplicationStatus status) {
}
