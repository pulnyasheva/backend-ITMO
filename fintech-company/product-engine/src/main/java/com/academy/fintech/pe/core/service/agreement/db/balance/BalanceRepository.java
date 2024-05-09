package com.academy.fintech.pe.core.service.agreement.db.balance;

import com.academy.fintech.pe.core.service.agreement.db.balance.entity.CompositeKey;
import com.academy.fintech.pe.core.service.agreement.db.balance.entity.EntityBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<EntityBalance, CompositeKey> {
}
