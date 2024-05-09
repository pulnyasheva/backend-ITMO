package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.balance.BalanceService;
import com.academy.fintech.processing.ProcessingRequest;
import com.academy.fintech.processing.ProcessingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProcessingService {

    private final BalanceService balanceService;

    public ProcessingResponse processing(ProcessingRequest request) {
        boolean successfullyAdded = balanceService.addClientPayment(mapRequestToPayment(request));

        if (!successfullyAdded) {
            return ProcessingResponse.newBuilder()
                    .setErrorMessage("The payment was not added to the balance")
                    .build();
        }

        return ProcessingResponse.newBuilder().build();
    }

    private ClientPayment mapRequestToPayment(ProcessingRequest request) {
        return ClientPayment.builder()
                .agreementId(request.getAgreementId())
                .amountPayment(new BigDecimal(request.getAmountPayment()))
                .build();
    }

}
