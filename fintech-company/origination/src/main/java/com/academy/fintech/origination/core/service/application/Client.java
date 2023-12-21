package com.academy.fintech.origination.core.service.application;

import lombok.Builder;

@Builder
public record Client(String id,
                     String firstName,
                     String lastName,
                     String email,
                     int salary) {
}
