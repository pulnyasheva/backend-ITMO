package com.academy.fintech.origination.core.pe.client.activation.grpc;

import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.disbursement.DisbursementResponse;
import com.academy.fintech.disbursement.DisbursementServiceGrpc;
import com.academy.fintech.disbursement.DisbursementServiceGrpc.DisbursementServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DisbursementGrpcClient {

    private final DisbursementServiceBlockingStub stub;

    public DisbursementGrpcClient(DisbursementGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = DisbursementServiceGrpc.newBlockingStub(channel);
    }

    public DisbursementResponse disbursement(DisbursementRequest request) {
        try {
            return stub.disbursement(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from PE disbursement by request: {}", request, e);
            throw e;
        }
    }
}
