package com.academy.fintech.pe.core.service.agreement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class Product {

    private String code;
    private int minLoanTerm;
    private int maxLoanTerm;
    private BigDecimal minPrincipalAmount;
    private BigDecimal maxPrincipalAmount;
    private BigDecimal minInterest;
    private BigDecimal maxInterest;
    private BigDecimal minOriginationAmount;
    private BigDecimal maxOriginationAmount;
}
