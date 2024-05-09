package com.academy.fintech.puller.core.service.exporter.application.db;

import com.academy.fintech.puller.core.service.exporter.application.db.entity.EntityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<EntityApplication, String> {

    List<EntityApplication> findAllByIdIn(List<String> ids);
}
