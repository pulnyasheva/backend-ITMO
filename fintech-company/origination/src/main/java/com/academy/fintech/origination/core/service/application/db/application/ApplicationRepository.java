package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.application.entity.EntityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<EntityApplication, String> {
    List<EntityApplication> findByClientId(String clientId);
    List<EntityApplication> findByStatus(ApplicationStatus status);
}
