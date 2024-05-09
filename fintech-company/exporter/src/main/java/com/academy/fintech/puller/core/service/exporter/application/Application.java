package com.academy.fintech.puller.core.service.exporter.application;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Application(String id,
                          String clientId,
                          int requestedDisbursementAmount,
                          ApplicationStatus status,
                          LocalDate updatedAt) {
}
