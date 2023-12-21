package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.origination.core.service.application.ApplicationDuplicate;
import com.academy.fintech.origination.core.service.application.ApplicationCreationService;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ApplicationController extends ApplicationServiceGrpc.ApplicationServiceImplBase {

    final private ApplicationCreationService createApplicationService;

    @Override
    public void create(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
        log.info("Got request application: {}", request);

        ApplicationDuplicate applicationDuplicate = createApplicationService.createNewApplication(request);


        if (!applicationDuplicate.isDuplicated()) {
            responseObserver.onNext(
                    ApplicationResponse.newBuilder()
                            .setApplicationId(applicationDuplicate.applicationId())
                            .build()
            );
            responseObserver.onCompleted();
        } else {
            Metadata metadata = new Metadata();
            metadata.put(Metadata.Key.of("applicationId", Metadata.ASCII_STRING_MARSHALLER),
                    applicationDuplicate.applicationId());
            responseObserver.onError(Status.ALREADY_EXISTS
                    .withDescription("Duplicate application")
                    .asRuntimeException(metadata));
        }

    }

}
