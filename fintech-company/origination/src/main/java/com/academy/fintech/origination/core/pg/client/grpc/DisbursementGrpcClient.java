package com.academy.fintech.origination.core.pg.client.grpc;

import com.academy.fintech.disbursement_processor.DisbursementProcessorRequest;
import com.academy.fintech.disbursement_processor.DisbursementProcessorResponse;
import com.academy.fintech.disbursement_processor.DisbursementProcessorServiceGrpc;
import com.academy.fintech.disbursement_processor.DisbursementProcessorServiceGrpc.DisbursementProcessorServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DisbursementGrpcClient {

    private final DisbursementProcessorServiceBlockingStub stub;

    public DisbursementGrpcClient(DisbursementGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = DisbursementProcessorServiceGrpc.newBlockingStub(channel);
    }

    public DisbursementProcessorResponse disburse(DisbursementProcessorRequest request) {
        try {
            return stub.disburse(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from PG processing by request: {}", request, e);
            throw e;
        }
    }
}
