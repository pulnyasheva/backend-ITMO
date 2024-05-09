package com.academy.fintech.pg.core.service.disbursement.db;

import com.academy.fintech.pg.core.service.disbursement.db.entity.EntityDisbursement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisbursementRepository extends JpaRepository<EntityDisbursement, String> {
}
