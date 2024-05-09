package com.academy.fintech.pe.core.service.agreement.db.balance;

import com.academy.fintech.pe.core.service.agreement.Balance;
import com.academy.fintech.pe.core.service.agreement.BalanceType;
import com.academy.fintech.pe.core.service.agreement.ClientPayment;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.EntityAgreement;
import com.academy.fintech.pe.core.service.agreement.db.balance.entity.CompositeKey;
import com.academy.fintech.pe.core.service.agreement.db.balance.entity.EntityBalance;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public void add(Balance balance) {
        balanceRepository.save(mapBalanceToEntity(balance));
    }

    /**
     * Добавляет на баланс клиента сумму, которую он внес
     */
    @Transactional
    public boolean addClientPayment(ClientPayment payment) {
        Optional<EntityBalance> balance = balanceRepository.findById(CompositeKey.builder()
                .agreementId(payment.agreementId())
                .type(BalanceType.CLIENT)
                .build());
        if (balance.isPresent()) {
            BigDecimal newAmount = balance.get().getAmount().add(payment.amountPayment());
            balance.get().setAmount(newAmount);
            balanceRepository.save(balance.get());
            return true;
        }
        return false;
    }

    public Optional<Balance> getTypeBalance(String agreementId, BalanceType type) {
        Optional<EntityBalance> balance = balanceRepository.findById(CompositeKey.builder()
                .agreementId(agreementId)
                .type(type).build());
        return balance.map(this::mapEntityToBalance);
    }

    @Transactional
    public void setAmount(String agreementId, BalanceType type, BigDecimal newAmount) {
        Optional<EntityBalance> balance = balanceRepository.findById(CompositeKey.builder()
                .agreementId(agreementId)
                .type(type).build());
        if (balance.isPresent()) {
            balance.get().setAmount(newAmount);
            balanceRepository.save(balance.get());
        }
    }

    private EntityBalance mapBalanceToEntity(Balance balance) {
        return EntityBalance.builder()
                .agreementId(balance.getAgreementId())
                .type(balance.getType())
                .amount(balance.getAmount())
                .build();
    }

    private Balance mapEntityToBalance(EntityBalance balance) {
        return Balance.builder()
                .agreementId(balance.getAgreementId())
                .type(balance.getType())
                .amount(balance.getAmount())
                .build();
    }
}
