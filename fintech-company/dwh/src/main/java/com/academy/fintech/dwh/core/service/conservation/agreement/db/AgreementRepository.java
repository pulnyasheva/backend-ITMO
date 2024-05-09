package com.academy.fintech.dwh.core.service.conservation.agreement.db;


import com.academy.fintech.dwh.core.service.conservation.agreement.db.entity.EntityAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<EntityAgreement, String> {
}
