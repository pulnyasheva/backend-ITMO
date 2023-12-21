package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.disbursement.DisbursementResponse;
import com.academy.fintech.disbursement.DisbursementServiceGrpc;
import com.academy.fintech.pe.core.service.agreement.AgreementDisbursementService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class DisbursementController extends DisbursementServiceGrpc.DisbursementServiceImplBase {

    private final AgreementDisbursementService agreementDisbursementService;

    @Override
    public void disbursement(DisbursementRequest request, StreamObserver<DisbursementResponse> responseObserver) {
        log.info("Got request to disbursement date: {}", request);

        responseObserver.onNext(
                agreementDisbursementService.disbursement(request)
        );
        responseObserver.onCompleted();
    }
}

