package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.overdue_payment.OverduePaymentRequest;
import com.academy.fintech.overdue_payment.OverduePaymentResponse;
import com.academy.fintech.overdue_payment.OverduePaymentServiceGrpc;
import com.academy.fintech.pe.core.service.agreement.PaymentService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class OverduePaymentController extends OverduePaymentServiceGrpc.OverduePaymentServiceImplBase {

    private final PaymentService paymentsService;

    @Override
    public void getOverduePayments(OverduePaymentRequest request, StreamObserver<OverduePaymentResponse> responseObserver) {
            log.info("Got request to overdue payments: {}", request);

            responseObserver.onNext(
                    paymentsService.getDateOverduePayments(request)
            );
            responseObserver.onCompleted();

    }
}
