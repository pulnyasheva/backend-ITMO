package com.academy.fintech.scoring.core.pe.client.grpc;

import com.academy.fintech.create_schedule.CreateScheduleRequest;
import com.academy.fintech.create_schedule.CreateScheduleResponse;
import com.academy.fintech.create_schedule.CreateScheduleServiceGrpc;
import com.academy.fintech.create_schedule.CreateScheduleServiceGrpc.CreateScheduleServiceBlockingStub;
import com.academy.fintech.overdue_payment.OverduePaymentRequest;
import com.academy.fintech.overdue_payment.OverduePaymentResponse;
import com.academy.fintech.overdue_payment.OverduePaymentServiceGrpc;
import com.academy.fintech.overdue_payment.OverduePaymentServiceGrpc.OverduePaymentServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PEGrpcClient {
    private final CreateScheduleServiceBlockingStub createScheduleServiceBlockingStub;

    private final OverduePaymentServiceBlockingStub overduePaymentServiceBlockingStub;

    public PEGrpcClient(PEGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.createScheduleServiceBlockingStub = CreateScheduleServiceGrpc.newBlockingStub(channel);
        this.overduePaymentServiceBlockingStub = OverduePaymentServiceGrpc.newBlockingStub(channel);
    }

    public CreateScheduleResponse createSchedule(CreateScheduleRequest request) {
        try {
            return createScheduleServiceBlockingStub.create(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from PE by request schedule: {}", request, e);
            throw e;
        }
    }

    public OverduePaymentResponse getOverduePayments(OverduePaymentRequest request) {
        try {
            return overduePaymentServiceBlockingStub.getOverduePayments(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from PE by request overdue payments: {}", request, e);
            throw e;
        }
    }

}
