package com.academy.fintech.pg.core.pe.client;

import com.academy.fintech.pg.core.service.processing.ClientPayment;
import com.academy.fintech.pg.core.pe.client.grpc.ProcessingGrpcClient;
import com.academy.fintech.processing.ProcessingRequest;
import com.academy.fintech.processing.ProcessingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcessingClientServer {

    private final ProcessingGrpcClient processingGrpcClient;

    public Optional<String> processing(ClientPayment payment) {
        ProcessingResponse response = processingGrpcClient.processing(mapPaymentToRequest(payment));
        if (response.hasErrorMessage()) {
            return Optional.of(response.getErrorMessage());
        } else {
            return Optional.empty();
        }
    }

    private ProcessingRequest mapPaymentToRequest(ClientPayment payment) {
        return ProcessingRequest.newBuilder()
                .setAgreementId(payment.agreementId())
                .setAmountPayment(payment.amountPayment())
                .build();
    }

}
