package com.academy.fintech.origination.core.service.application.db.client;

import com.academy.fintech.origination.core.service.application.db.client.entity.EntityClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<EntityClient, String> {
    List<EntityClient> findByEmail(String email);
}
