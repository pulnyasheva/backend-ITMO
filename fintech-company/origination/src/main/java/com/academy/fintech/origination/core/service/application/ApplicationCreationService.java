package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationCreationService {
    private final ClientService clientService;

    private final ApplicationService applicationService;

    @Transactional
    public ApplicationDuplicate createNewApplication(ApplicationRequest request) {
        String clientId = clientService.getOrCreate(mapRequestToClient(request));

        Optional<String> applicationId = isApplicationAlready(clientId, request.getDisbursementAmount());

        // Создаем заявку, если такой еще нет
        if (applicationId.isEmpty()) {
            applicationId = Optional.of(createApplication(request, clientId));
            return ApplicationDuplicate.builder()
                    .applicationId(applicationId.get())
                    .isDuplicated(false)
                    .build();
        }

        return ApplicationDuplicate.builder()
                .applicationId(applicationId.get())
                .isDuplicated(true)
                .build();
    }

    private Client mapRequestToClient(ApplicationRequest request) {
        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .salary(request.getSalary())
                .build();
        return client;
    }

    /**
     * Метод проверяет есть ли уже у клиента заявка со статусом NEW и такой же суммой
     */
    private Optional<String> isApplicationAlready(String clientId, int disbursementAmount) {
        List<Application> applications = applicationService.getNewApplicationClient(clientId);
        for (Application application : applications) {
            if (isNewStatusApplication(application) && isDisbursementAmountEquals(application, disbursementAmount))
                return Optional.of(application.id());
        }
        return Optional.empty();
    }

    private boolean isNewStatusApplication(Application application) {
        return application.status().equals(ApplicationStatus.NEW);
    }

    private boolean isDisbursementAmountEquals(Application application, int disbursementAmount) {
        return application.requestedDisbursementAmount() == disbursementAmount;
    }

    private String createApplication(ApplicationRequest request, String clientId) {
        Application application = Application.builder()
                .clientId(clientId)
                .requestedDisbursementAmount(request.getDisbursementAmount())
                .status(ApplicationStatus.NEW)
                .build();
        return applicationService.add(application);
    }
}