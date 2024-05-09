package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.Application;
import com.academy.fintech.origination.core.service.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.application.entity.EntityApplication;
import com.academy.fintech.origination.core.service.export.ExportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    final private ApplicationRepository applicationRepository;
    final private ExportService exportService;

    public String add(Application application) {
        EntityApplication entityApplication = mapApplicationToEntity(application);
        entityApplication.setUpdatedAt(LocalDate.now());
        return applicationRepository.save(entityApplication).getId();
    }

    /**
     * Метод возвращает все заявки со статусом NEW у клиента
     */
    public List<Application> getNewApplicationClient(String clientId) {
        List<Application> applications = new ArrayList<>();
        for (EntityApplication entityApplication : applicationRepository.findByClientId(clientId)) {
            applications.add(mapEntityToApplication(entityApplication));
        }
        return applications;
    }

    /**
     * Метод возвращает все заявки со статусом NEW
     */
    public List<Application> getNewApplication() {
        List<Application> applications = new ArrayList<>();
        for (EntityApplication entityApplication : applicationRepository.findByStatus(ApplicationStatus.NEW)) {
            applications.add(mapEntityToApplication(entityApplication));
        }
        return applications;
    }

    /**
     * Метод обновляет статус и создает задачу для выгрузки в kafka
     */
    @Transactional
    public boolean updateStatus(String applicationId, ApplicationStatus newStatus) {
        Optional<EntityApplication> application = applicationRepository.findById(applicationId);
        if (application.isPresent()) {
            application.get().setStatus(newStatus);
            application.get().setUpdatedAt(LocalDate.now());
            applicationRepository.save(application.get());
            exportService.createTask(mapEntityToApplication(application.get()));
            return true;
        }
        return false;
    }

    private EntityApplication mapApplicationToEntity(Application application) {
        return EntityApplication.builder()
                .clientId(application.clientId())
                .requestedDisbursementAmount(application.requestedDisbursementAmount())
                .status(application.status())
                .build();
    }

    private Application mapEntityToApplication(EntityApplication entityApplication) {
        return Application.builder()
                .id(entityApplication.getId())
                .clientId(entityApplication.getClientId())
                .requestedDisbursementAmount(entityApplication.getRequestedDisbursementAmount())
                .status(entityApplication.getStatus())
                .build();
    }
}
