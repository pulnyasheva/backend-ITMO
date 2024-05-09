package com.academy.fintech.origination.core.service.disbursement;

import com.academy.fintech.origination.core.pg.client.DisbursementClientService;
import com.academy.fintech.origination.core.service.application.Application;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisbursementService {

    private final DisbursementClientService disbursementClientService;
    private final ClientService clientService;

    public void issue(Application application, String agreementId) {
        boolean paymentAccepted = disbursementClientService.disburse(createDisbursement(application, agreementId));
        if (!paymentAccepted) {
            log.error("Issue of the amount has not been accepted");
        }
    }

    private Disbursement createDisbursement(Application application, String agreementId) {
        Optional<String> clientEmail = clientService.getEmail(application.clientId());
        if (clientEmail.isPresent()) {
            return Disbursement.builder()
                    .clientEmail(clientEmail.get())
                    .agreementId(agreementId)
                    .amount(application.requestedDisbursementAmount())
                    .build();
        } else {
            throw new NoSuchElementException("Client not found");
        }
    }
}
