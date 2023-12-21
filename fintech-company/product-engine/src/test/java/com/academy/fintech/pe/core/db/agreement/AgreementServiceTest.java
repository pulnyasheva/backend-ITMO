package com.academy.fintech.pe.core.db.agreement;

import com.academy.fintech.pe.core.db.agreement.entity.EntityAgreementTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgreementServiceTest {

    @Autowired
    private AgreementRepositoryTest agreementRepository;

    public void add(EntityAgreementTest agreement){
        agreementRepository.save(agreement);
    }

    public Optional<EntityAgreementTest> get(String agreementId){
        return agreementRepository.findById(agreementId);
    }
}
