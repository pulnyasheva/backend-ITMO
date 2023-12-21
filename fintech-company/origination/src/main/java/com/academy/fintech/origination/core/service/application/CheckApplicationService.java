package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.scoring.client.ScoringClientService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckApplicationService {
    private final int DELAY = 10000;
    private final ApplicationService applicationService;
    private final CheckScoringApplicationService checkScoringApplicationService;

    private final ClientService clientService;
    private final EmailService emailService;


    @Scheduled(fixedDelay = DELAY)
    @Transactional
    public void checkApplications() {
        List<Application> applications = applicationService.getNewApplication();

        for (Application application : applications) {
            applicationService.updateStatus(application.id(), ApplicationStatus.SCORING);

            try {
                ApplicationStatus status = checkScoringApplicationService.checkApplication(application);
                emailService.sendEmailForStatus(clientService.get(application.clientId()).get().email(), status);
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }
        }
    }
}
