package com.academy.fintech.origination.core.db.client;

import com.academy.fintech.origination.core.db.client.entity.EntityClientTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepositoryTest extends JpaRepository<EntityClientTest, String> {
    List<EntityClientTest> findByEmail(String email);
}

