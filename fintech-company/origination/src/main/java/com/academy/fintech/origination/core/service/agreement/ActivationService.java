package com.academy.fintech.origination.core.service.agreement;

import com.academy.fintech.create_agreement.ActivationRequest;
import com.academy.fintech.create_agreement.ActivationResponse;
import com.academy.fintech.origination.core.pe.client.activation.DisbursementClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ActivationService {

    private final DisbursementClientService disbursementClientService;

    public ActivationResponse activate(ActivationRequest request) {
        String scheduleId = disbursementClientService.disbursement(mapRequestToApplication(request));
        if (scheduleId != null) {
            return ActivationResponse.newBuilder()
                    .setSuccessfully(true)
                    .build();
        }
        return ActivationResponse.newBuilder()
                .setSuccessfully(false)
                .build();
    }

    private ApplicationActivation mapRequestToApplication(ActivationRequest request) {
        return ApplicationActivation.builder()
                .agreementId(request.getAgreementId())
                .disbursementDate(
                        LocalDate.of(request.getDisbursementDate().getYear(),
                                request.getDisbursementDate().getMonth(),
                                request.getDisbursementDate().getDay()))
                .build();
    }
}
