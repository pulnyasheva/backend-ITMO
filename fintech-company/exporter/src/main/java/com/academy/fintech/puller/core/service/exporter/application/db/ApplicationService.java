package com.academy.fintech.puller.core.service.exporter.application.db;

import com.academy.fintech.puller.core.service.exporter.ObjectService;
import com.academy.fintech.puller.core.service.exporter.application.Application;
import com.academy.fintech.puller.core.service.exporter.application.db.entity.EntityApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService implements ObjectService<Application, String> {

    private final ApplicationRepository applicationRepository;

    @Override
    public List<Application> getByIds(List<String> ids) {
        return mapEntityListToApplicationList(applicationRepository.findAllByIdIn(ids));
    }

    private Application mapEntityToApplication(EntityApplication entityApplication) {
        return Application.builder()
                .id(entityApplication.getId())
                .clientId(entityApplication.getClientId())
                .requestedDisbursementAmount(entityApplication.getRequestedDisbursementAmount())
                .status(entityApplication.getStatus())
                .updatedAt(entityApplication.getUpdatedAt())
                .build();
    }

    private List<Application> mapEntityListToApplicationList(List<EntityApplication> entityApplications) {
        List<Application> applications = new ArrayList<>();
        for (EntityApplication entityApplication : entityApplications) {
            applications.add(mapEntityToApplication(entityApplication));
        }
        return applications;
    }
}
