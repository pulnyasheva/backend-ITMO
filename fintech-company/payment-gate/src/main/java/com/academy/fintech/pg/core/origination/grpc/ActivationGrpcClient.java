package com.academy.fintech.pg.core.origination.grpc;

import com.academy.fintech.create_agreement.ActivationRequest;
import com.academy.fintech.create_agreement.ActivationResponse;
import com.academy.fintech.create_agreement.ActivationServiceGrpc;
import com.academy.fintech.create_agreement.ActivationServiceGrpc.ActivationServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActivationGrpcClient {

    private final ActivationServiceBlockingStub stub;

    public ActivationGrpcClient(ActivationGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ActivationServiceGrpc.newBlockingStub(channel);
    }

    public ActivationResponse activate(ActivationRequest request) {
        try {
            return stub.activate(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from PE activate agreement: {}", request, e);
            throw e;
        }
    }
}
