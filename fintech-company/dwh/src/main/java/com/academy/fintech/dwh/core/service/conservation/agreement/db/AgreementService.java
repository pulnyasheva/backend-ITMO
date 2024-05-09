package com.academy.fintech.dwh.core.service.conservation.agreement.db;

import com.academy.fintech.dwh.core.service.conservation.agreement.Agreement;
import com.academy.fintech.dwh.core.service.conservation.agreement.db.entity.EntityAgreement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;

    public void save(Agreement agreement) {
        agreementRepository.save(mapAgreementToEntity(agreement));
    }

    private EntityAgreement mapAgreementToEntity(Agreement agreement) {
        return EntityAgreement.builder()
                .id(agreement.id())
                .productCode(agreement.productCode())
                .clientId(agreement.clientId())
                .interest(agreement.interest())
                .loanTerm(agreement.loanTerm())
                .principalAmount(agreement.principalAmount())
                .originationAmount(agreement.originationAmount())
                .status(agreement.status())
                .disbursementDate(agreement.disbursementDate())
                .nextPaymentDate(agreement.nextPaymentDate())
                .updatedAt(agreement.updatedAt())
                .build();
    }
}
