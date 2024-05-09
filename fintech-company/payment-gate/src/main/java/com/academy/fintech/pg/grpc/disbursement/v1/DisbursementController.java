package com.academy.fintech.pg.grpc.disbursement.v1;

import com.academy.fintech.disbursement_processor.DisbursementProcessorRequest;
import com.academy.fintech.disbursement_processor.DisbursementProcessorResponse;
import com.academy.fintech.disbursement_processor.DisbursementProcessorServiceGrpc;
import com.academy.fintech.pg.core.service.disbursement.DisbursementProcessorService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class DisbursementController extends DisbursementProcessorServiceGrpc.DisbursementProcessorServiceImplBase {

    private final DisbursementProcessorService issuanceService;

    @Override
    public void disburse(DisbursementProcessorRequest request, StreamObserver<DisbursementProcessorResponse> responseObserver) {
        log.info("Got request to issue payment: {}", request);

        responseObserver.onNext(
                issuanceService.disburse(request)
        );
        responseObserver.onCompleted();
    }
}
