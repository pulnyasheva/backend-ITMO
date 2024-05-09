package com.academy.fintech.pg.core.service.processing;

import com.academy.fintech.pg.core.pe.client.ProcessingClientServer;
import com.academy.fintech.pg.rest.processing.ProcessingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessingService {

    private final ProcessingClientServer processingClientServer;

    /**
     * Запрашивает выполнение операции пополнения счета клиента на сумму, которую внес клиент
     */
    public boolean processing(ProcessingRequest request) {
        Optional<String> errorMessage = processingClientServer.processing(mapRequestToPayment(request));
        if (errorMessage.isPresent()) {
            log.error("Payment processing error: {}", errorMessage.get());
            return false;
        }
        return true;
    }

    private ClientPayment mapRequestToPayment(ProcessingRequest request) {
        return ClientPayment.builder()
                .agreementId(request.agreementId())
                .amountPayment(request.amountPayment())
                .build();
    }
}
