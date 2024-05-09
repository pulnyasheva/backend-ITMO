package com.academy.fintech.dwh.core.service.conservation.application.db;


import com.academy.fintech.dwh.core.service.conservation.application.db.entity.EntityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<EntityApplication, String> {
}
