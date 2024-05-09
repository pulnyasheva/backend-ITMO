package com.academy.fintech.origination.core.service.agreement;

import com.academy.fintech.origination.core.pe.client.creation.CreationAgreementClientService;
import com.academy.fintech.origination.core.service.application.Application;
import com.academy.fintech.origination.core.service.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.constants.CreditConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreationAgreementService {

    private final CreationAgreementClientService creationAgreementClientService;

    /**
     * Запрос на активацию договора
     *
     * @param application заявка, по которой нужно активировать договор
     * @param status      статус заявки
     */
    public CreationAgreementResult create(Application application, ApplicationStatus status) {
        if (status == ApplicationStatus.ACCEPTED) {
            return creationAgreementClientService.createAgreement(mapApplicationToAgreement(application));

        } else {
            final String errorMessage = "Application closed";
            return CreationAgreementResult.builder()
                    .created(false)
                    .errorMessage(errorMessage)
                    .build();
        }
    }

    private Agreement mapApplicationToAgreement(Application application) {
        return Agreement.builder()
                .productCode(CreditConstants.PRODUCT_CODE)
                .clientId(application.clientId())
                .interest(CreditConstants.INTEREST)
                .loanTerm(CreditConstants.LOAN_TERM)
                .disbursementAmount(new BigDecimal(application.requestedDisbursementAmount()))
                .originationAmount(CreditConstants.ORIGINATION_AMOUNT)
                .build();
    }
}
