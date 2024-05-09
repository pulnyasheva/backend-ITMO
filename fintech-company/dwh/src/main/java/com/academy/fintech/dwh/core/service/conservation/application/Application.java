package com.academy.fintech.dwh.core.service.conservation.application;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Application(String id,
                          String clientId,
                          int requestedDisbursementAmount,
                          ApplicationStatus status,
                          LocalDate updateAt) {
}
