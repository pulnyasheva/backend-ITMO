package com.academy.fintech.puller.core.service.exporter.agreement.db;

import com.academy.fintech.puller.core.service.exporter.agreement.db.entity.EntityAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<EntityAgreement, String> {

    List<EntityAgreement> findAllByIdIn(List<String> ids);
}
