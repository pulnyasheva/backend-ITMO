package com.academy.fintech.pe.core.db.balance;

import com.academy.fintech.pe.core.db.balance.entity.CompositeKeyTest;
import com.academy.fintech.pe.core.db.balance.entity.EntityBalanceTest;
import com.academy.fintech.pe.core.service.agreement.Balance;
import com.academy.fintech.pe.core.service.agreement.db.balance.entity.EntityBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceServiceTest {

    private final BalanceRepositoryTest balanceRepositoryTest;

    public void add(EntityBalanceTest balance) {
        balanceRepositoryTest.save(balance);
    }

    public Optional<EntityBalanceTest> get(CompositeKeyTest id) {
        return balanceRepositoryTest.findById(id);
    }
}
