package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.scoring.client.ScoringClientService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckScoringApplicationService {

    private final BigDecimal INTEREST = new BigDecimal(10);
    private final int LOAN_TERM = 12;

    private final ApplicationService applicationService;
    private final ClientService clientService;
    private final ScoringClientService scoringClientService;


    /**
     * Отправление NEW {@link Application} для проверки на scoring. Статус заявки меняется на SCORING.
     * <ul>
     *   <li> Если кредитный рейтинг клиента положительный, то статус заявки меняется на ACCEPTED.</li>
     *   <li> Если кредитный рейтинг клиента не более 0, то статус заявки меняется на CLOSED.</li>
     * </ul>
     */
    public ApplicationStatus checkApplication(Application application) throws NoSuchElementException {
        applicationService.updateStatus(application.id(), ApplicationStatus.SCORING);

        Optional<Client> optionalClient = clientService.get(application.clientId());
        if (optionalClient.isEmpty()) {
            throw new NoSuchElementException("Client not found");
        }

        Client client = optionalClient.get();
        int score = scoringClientService.scoring(createScoringApplication(application, client.salary()));

        if (score > 0) {
            applicationService.updateStatus(application.id(), ApplicationStatus.ACCEPTED);
            return ApplicationStatus.ACCEPTED;
        } else {
            applicationService.updateStatus(application.id(), ApplicationStatus.CLOSED);
            return ApplicationStatus.CLOSED;
        }
    }

    private ScoringApplication createScoringApplication(Application application, int clientSalary) {
        return ScoringApplication.builder()
                .clientId(application.clientId())
                .clientSalary(clientSalary)
                .loanTerm(LOAN_TERM)
                .disbursementAmount(application.requestedDisbursementAmount())
                .interest(INTEREST).build();
    }
}
