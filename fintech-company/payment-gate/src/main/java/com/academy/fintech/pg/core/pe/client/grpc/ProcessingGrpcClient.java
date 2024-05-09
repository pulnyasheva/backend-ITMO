package com.academy.fintech.pg.core.pe.client.grpc;

import com.academy.fintech.processing.ProcessingRequest;
import com.academy.fintech.processing.ProcessingResponse;
import com.academy.fintech.processing.ProcessingServiceGrpc;
import com.academy.fintech.processing.ProcessingServiceGrpc.ProcessingServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessingGrpcClient {

    private final ProcessingServiceBlockingStub stub;

    public ProcessingGrpcClient(ProcessingGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ProcessingServiceGrpc.newBlockingStub(channel);
    }

    public ProcessingResponse processing(ProcessingRequest request) {
        try {
            return stub.processing(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from PE payment by request: {}", request, e);
            throw e;
        }
    }
}
