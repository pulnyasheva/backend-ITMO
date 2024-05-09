package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.EntityAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<EntityAgreement, String> {
    List<EntityAgreement> findByClientId(String clientId);

    List<EntityAgreement> findByStatus(AgreementStatus status);
}
