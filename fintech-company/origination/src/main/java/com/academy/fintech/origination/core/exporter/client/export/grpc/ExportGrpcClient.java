package com.academy.fintech.origination.core.exporter.client.export.grpc;

import com.academy.fintech.exporter.ExporterRequest;
import com.academy.fintech.exporter.ExporterResponse;
import com.academy.fintech.exporter.ExporterServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExportGrpcClient {

    private final ExporterServiceGrpc.ExporterServiceBlockingStub stub;

    public ExportGrpcClient(ExportGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ExporterServiceGrpc.newBlockingStub(channel);
    }

    public ExporterResponse createTask(ExporterRequest request) {
        try {
            return stub.createTask(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from exporter createTask by request: {}", request, e);
            throw e;
        }
    }
}
