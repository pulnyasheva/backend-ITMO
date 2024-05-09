package com.academy.fintech.origination.core.pe.client.creation;

import com.academy.fintech.create.CreateRequest;
import com.academy.fintech.create.CreateResponse;
import com.academy.fintech.origination.core.pe.client.creation.grpc.CreationAgreementGrpcClient;
import com.academy.fintech.origination.core.service.agreement.Agreement;
import com.academy.fintech.origination.core.service.agreement.CreationAgreementResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreationAgreementClientService {

    private final CreationAgreementGrpcClient creationAgreementGrpcClient;

    /**
     * Запрос на создание договора
     *
     * @return результат создание договора {@link CreationAgreementResult}, где указано создан ли договор
     * <ul>
     *     <li>Если договор создан, то указан id договора</li>
     *     <li>Если договор не создан, то указана ошибка</li>
     * </ul>
     */
    public CreationAgreementResult createAgreement(Agreement agreement) {
        CreateRequest request = mapAgreementToRequest(agreement);
        CreateResponse response = creationAgreementGrpcClient.createAgreement(request);

        if (response.hasAgreementId()) {
            return CreationAgreementResult.builder()
                    .created(true)
                    .agreementId(response.getAgreementId())
                    .build();
        } else {
            return CreationAgreementResult.builder()
                    .created(false)
                    .errorMessage(response.getErrorMessage())
                    .build();
        }
    }

    private static CreateRequest mapAgreementToRequest(Agreement agreement) {
        return CreateRequest.newBuilder()
                .setClientId(agreement.clientId())
                .setLoanTerm(agreement.loanTerm())
                .setDisbursementAmount(agreement.disbursementAmount().toString())
                .setOriginationAmount(agreement.originationAmount().toString())
                .setInterest(agreement.interest().toString())
                .setProductCode(agreement.productCode())
                .build();
    }
}
