package com.academy.fintech.origination.core.db.application;

import com.academy.fintech.origination.core.db.application.entity.EntityApplicationTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepositoryTest extends JpaRepository<EntityApplicationTest, String> {
}
