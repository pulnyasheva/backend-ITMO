package com.academy.fintech.scoring.grpc.v1;

import com.academy.fintech.check_scoring.CheckScoringRequest;
import com.academy.fintech.check_scoring.CheckScoringResponse;
import com.academy.fintech.check_scoring.CheckScoringServiceGrpc;
import com.academy.fintech.scoring.core.service.application.ScoringService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class CheckScoringController extends CheckScoringServiceGrpc.CheckScoringServiceImplBase {

    private final ScoringService scoringService;

    @Override
    public void scoring(CheckScoringRequest request, StreamObserver<CheckScoringResponse> responseObserver) {
        log.info("Got request to create a schedule for scoring: {}", request);

        responseObserver.onNext(
                scoringService.scoring(request)
        );
        responseObserver.onCompleted();

    }
}




