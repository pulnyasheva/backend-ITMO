package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.Application;
import com.academy.fintech.origination.core.service.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.application.entity.EntityApplication;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    final private ApplicationRepository applicationRepository;

    public String add(Application application) {
        return applicationRepository.save(mapApplicationToEntity(application)).getId();
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

    @Transactional
    public boolean updateStatus(String applicationId, ApplicationStatus newStatus) {
        Optional<EntityApplication> application = applicationRepository.findById(applicationId);
        if (application.isPresent()) {
            application.get().setStatus(newStatus);
            applicationRepository.save(application.get());
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
