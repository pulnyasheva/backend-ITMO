package com.academy.fintech.pg.core.service.disbursement.db;

import com.academy.fintech.pg.core.service.disbursement.Disbursement;
import com.academy.fintech.pg.core.service.disbursement.DisbursementStatus;
import com.academy.fintech.pg.core.service.disbursement.db.entity.EntityDisbursement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisbursementService {

    private final DisbursementRepository issuancePaymentRepository;

    public void add(Disbursement payment) {
        issuancePaymentRepository.save(mapDisburseToEntity(payment));
    }

    @Transactional
    public void changeStatus(String paymentId, DisbursementStatus status) {
        Optional<EntityDisbursement> payment = issuancePaymentRepository.findById(paymentId);
        if (payment.isPresent()) {
            payment.get().setStatus(status);
            issuancePaymentRepository.save(payment.get());
        }
    }

    private EntityDisbursement mapDisburseToEntity(Disbursement disbursement) {
        return EntityDisbursement.builder()
                .id(disbursement.getId())
                .agreementId(disbursement.getAgreementId())
                .clientEmail(disbursement.getClientEmail())
                .amount(new BigDecimal(disbursement.getAmount()))
                .status(DisbursementStatus.WAIT)
                .build();
    }
}
