package com.academy.fintech.origination.core.pe.client.creation.grpc;

import com.academy.fintech.create.CreateRequest;
import com.academy.fintech.create.CreateResponse;
import com.academy.fintech.create.CreateServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.academy.fintech.create.CreateServiceGrpc.CreateServiceBlockingStub;

@Slf4j
@Component
public class CreationAgreementGrpcClient {

    private final CreateServiceBlockingStub stub;

    public CreationAgreementGrpcClient(CreationAgreementGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = CreateServiceGrpc.newBlockingStub(channel);
    }

    public CreateResponse createAgreement(CreateRequest request) {
        try {
            return stub.create(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from PE create agreement by request: {}", request, e);
            throw e;
        }
    }
}
