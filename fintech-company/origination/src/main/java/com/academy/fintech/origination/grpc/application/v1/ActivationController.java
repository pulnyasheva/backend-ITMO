package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.create_agreement.ActivationRequest;
import com.academy.fintech.create_agreement.ActivationResponse;
import com.academy.fintech.create_agreement.ActivationServiceGrpc;
import com.academy.fintech.origination.core.service.agreement.ActivationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ActivationController extends ActivationServiceGrpc.ActivationServiceImplBase {

    private final ActivationService activationService;

    @Override
    public void activate(ActivationRequest request, StreamObserver<ActivationResponse> responseObserver) {
        log.info("Got request activate agreement in Origination: {}", request);

        responseObserver.onNext(
                activationService.activate(request)
        );
        responseObserver.onCompleted();
    }
}
