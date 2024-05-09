package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.calculation.payment_schedule.Functions;
import com.academy.fintech.pe.core.service.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.EntityAgreement;
import com.academy.fintech.pe.core.service.export.ExportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;
    private final ExportService exportService;

    public String add(Agreement agreement) {
        EntityAgreement entityAgreement = mapAgreementToEntity(agreement);
        entityAgreement.setUpdatedAt(LocalDate.now());
        return agreementRepository.save(entityAgreement).getId();
    }

    /**
     * Изменяет статус договора и создает задачу для выгрузки в kafka
     */
    @Transactional
    public void setStatus(String agreementId, AgreementStatus newStatus) {
        Optional<EntityAgreement> agreement = agreementRepository.findById(agreementId);
        if (agreement.isPresent()) {
            agreement.get().setStatus(newStatus);
            agreement.get().setUpdatedAt(LocalDate.now());
            agreementRepository.save(agreement.get());
            exportService.createTask(mapEntityToAgreement(agreement.get()));
        }
    }

    /**
     * Устанавливает дату выдачи кредита, изменяет статус договора на {@link AgreementStatus#ACTIVE} и
     * рассчитывает дату следующего платежа.
     *
     * @param agreementId      Идентификатор договора.
     * @param disbursementDate Дата выдачи кредита.
     */
    @Transactional
    public void setDisbursementDate(String agreementId, LocalDate disbursementDate) {
        Optional<EntityAgreement> agreement = agreementRepository.findById(agreementId);
        if (agreement.isPresent()) {
            LocalDate nextPaymentDate = Functions.calculateNextPaymentDate(disbursementDate);
            agreement.get().setDisbursementDate(disbursementDate);
            agreement.get().setNextPaymentDate(nextPaymentDate);
            setStatus(agreementId, AgreementStatus.ACTIVE);
            agreementRepository.save(agreement.get());
        }
    }

    public List<Agreement> getByClientId(String clientId) {
        return mapEntityListToAgreementList(agreementRepository.findByClientId(clientId));
    }

    public Agreement get(String agreementId) {
        Optional<EntityAgreement> agreement = agreementRepository.findById(agreementId);
        return agreement.map(this::mapEntityToAgreement).orElse(null);
    }

    public List<Agreement> getActiveAgreement() {
        return mapEntityListToAgreementList(agreementRepository.findByStatus(AgreementStatus.ACTIVE));
    }

    @Transactional
    public void setNextPayment(String agreementId, LocalDate nextPaymentDate) {
        Optional<EntityAgreement> agreement = agreementRepository.findById(agreementId);
        if (agreement.isPresent()) {
            agreement.get().setNextPaymentDate(nextPaymentDate);
            agreementRepository.save(agreement.get());
        }
    }

    private EntityAgreement mapAgreementToEntity(Agreement agreement) {
        return EntityAgreement.builder()
                .productCode(agreement.productCode())
                .clientId(agreement.clientId())
                .interest(agreement.interest())
                .loanTerm(agreement.loanTerm())
                .principalAmount(agreement.principalAmount())
                .originationAmount(agreement.originationAmount())
                .status(agreement.status())
                .disbursementDate(agreement.disbursementDate())
                .nextPaymentDate(agreement.nextPaymentDate())
                .build();
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
