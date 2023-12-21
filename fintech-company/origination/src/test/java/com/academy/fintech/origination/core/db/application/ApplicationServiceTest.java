package com.academy.fintech.origination.core.db.application;

import com.academy.fintech.origination.core.db.application.entity.EntityApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationServiceTest {

    @Autowired
    private ApplicationRepositoryTest applicationRepository;

    public Optional<EntityApplicationTest> getApplication(String applicationId) {
        return applicationRepository.findById(applicationId);
    }
}
