package com.academy.fintech.pe.core.db.balance;

import com.academy.fintech.pe.core.db.balance.entity.CompositeKeyTest;
import com.academy.fintech.pe.core.db.balance.entity.EntityBalanceTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepositoryTest extends JpaRepository<EntityBalanceTest, CompositeKeyTest> {
}
