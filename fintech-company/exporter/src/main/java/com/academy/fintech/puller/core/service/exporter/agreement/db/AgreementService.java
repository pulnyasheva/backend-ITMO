package com.academy.fintech.puller.core.service.exporter.agreement.db;

import com.academy.fintech.puller.core.service.exporter.ObjectService;
import com.academy.fintech.puller.core.service.exporter.agreement.Agreement;
import com.academy.fintech.puller.core.service.exporter.agreement.db.entity.EntityAgreement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementService implements ObjectService<Agreement, String> {

    private final AgreementRepository agreementRepository;

    @Override
    public List<Agreement> getByIds(List<String> ids) {
        return mapEntityListToAgreementList(agreementRepository.findAllByIdIn(ids));
    }

    private Agreement mapEntityToAgreement(EntityAgreement entityAgreement) {
        return Agreement.builder()
                .id(entityAgreement.getId())
                .productCode(entityAgreement.getProductCode())
                .clientId(entityAgreement.getClientId())
                .interest(entityAgreement.getInterest())
                .loanTerm(entityAgreement.getLoanTerm())
                .principalAmount(entityAgreement.getPrincipalAmount())
                .originationAmount(entityAgreement.getOriginationAmount())
                .status(entityAgreement.getStatus())
                .disbursementDate(entityAgreement.getDisbursementDate())
                .nextPaymentDate(entityAgreement.getNextPaymentDate())
                .updatedAt(entityAgreement.getUpdatedAt())
                .build();
    }

    private List<Agreement> mapEntityListToAgreementList(List<EntityAgreement> entityAgreements) {
        List<Agreement> agreements = new ArrayList<>();
        for (EntityAgreement entityAgreement : entityAgreements) {
            agreements.add(mapEntityToAgreement(entityAgreement));
        }
        return agreements;
    }

}
