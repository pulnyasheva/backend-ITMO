package com.academy.fintech.pg.core.service.agreement;

import com.academy.fintech.pg.core.origination.ActivationClientServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationAgreementService {

    private final ActivationClientServer activationClientServer;

    /**
     * Запрашивает активацию договора
     *
     * @param application заявка на активацию договора
     */
    public void activate(ApplicationActivation application) {
        boolean successfullyCreated = activationClientServer.activate(application);
        if (!successfullyCreated) {
            log.error("Activate agreement error with id: {}", application.agreementId());
        }
    }


}
