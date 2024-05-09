package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.pe.core.service.agreement.ProcessingService;
import com.academy.fintech.processing.ProcessingRequest;
import com.academy.fintech.processing.ProcessingResponse;
import com.academy.fintech.processing.ProcessingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ProcessingController extends ProcessingServiceGrpc.ProcessingServiceImplBase {

    private final ProcessingService processingService;

    @Override
    public void processing(ProcessingRequest request, StreamObserver<ProcessingResponse> responseObserver) {
        log.info("Got request to client payment: {}", request);

        responseObserver.onNext(
                processingService.processing(request)
        );
        responseObserver.onCompleted();
    }
}
