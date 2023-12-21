package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.origination.core.service.application.RejectApplicationService;
import com.academy.fintech.rejection.RejectRequest;
import com.academy.fintech.rejection.RejectResponse;
import com.academy.fintech.rejection.RejectServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class RejectController extends RejectServiceGrpc.RejectServiceImplBase {

    private final RejectApplicationService rejectApplicationService;

    @Override
    public void reject(RejectRequest request, StreamObserver<RejectResponse> responseObserver) {
        log.info("Got request reject application: {}", request);

        responseObserver.onNext(
                rejectApplicationService.rejectApplication(request)
        );
        responseObserver.onCompleted();
    }
}

