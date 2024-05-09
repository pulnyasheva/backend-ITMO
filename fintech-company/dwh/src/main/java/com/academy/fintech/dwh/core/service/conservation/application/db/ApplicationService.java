package com.academy.fintech.dwh.core.service.conservation.application.db;

import com.academy.fintech.dwh.core.service.conservation.application.Application;
import com.academy.fintech.dwh.core.service.conservation.application.db.entity.EntityApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public void save(Application application) {
        applicationRepository.save(mapApplicationToEntity(application));
    }

    private EntityApplication mapApplicationToEntity(Application application) {
        return EntityApplication.builder()
                .id(application.id())
                .clientId(application.clientId())
                .requestedDisbursementAmount(application.requestedDisbursementAmount())
                .status(application.status())
                .updatedAt(application.updateAt())
                .build();
    }
}
