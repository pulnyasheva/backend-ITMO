package com.academy.fintech.puller.grpc.exporter.v1;

import com.academy.fintech.exporter.ExporterRequest;
import com.academy.fintech.exporter.ExporterResponse;
import com.academy.fintech.exporter.ExporterServiceGrpc;
import com.academy.fintech.puller.core.service.export_task.ExporterService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ExporterController extends ExporterServiceGrpc.ExporterServiceImplBase {

    private final ExporterService exporterService;

    @Override
    public void createTask(ExporterRequest request, StreamObserver<ExporterResponse> responseObserver) {
        log.info("Got request create export task in Exporter: {}", request);

        responseObserver.onNext(
                exporterService.createTask(request)
        );
        responseObserver.onCompleted();
    }
}
