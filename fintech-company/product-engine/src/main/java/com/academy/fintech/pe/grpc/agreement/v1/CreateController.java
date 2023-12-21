package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.create.CreateRequest;
import com.academy.fintech.create.CreateResponse;
import com.academy.fintech.create.CreateServiceGrpc;
import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class CreateController extends CreateServiceGrpc.CreateServiceImplBase {

    private final AgreementCreationService agreementCreationService;

    @Override
    public void create(CreateRequest request, StreamObserver<CreateResponse> responseObserver) {
        log.info("Got request to create an agreement: {}", request);

        CreateResponse response;
        try {
            response = agreementCreationService.createAgreement(request);
        } catch (Exception e) {
            response = CreateResponse.newBuilder()
                    .setErrorMessage(e.getMessage())
                    .build();
        }

        responseObserver.onNext(
                response
        );
        responseObserver.onCompleted();
    }
}

