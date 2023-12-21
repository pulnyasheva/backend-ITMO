package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.create_schedule.CreateScheduleRequest;
import com.academy.fintech.create_schedule.CreateScheduleResponse;
import com.academy.fintech.create_schedule.CreateScheduleServiceGrpc;
import com.academy.fintech.pe.core.service.agreement.ScoringCreationScheduleService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class CreateScheduleController extends CreateScheduleServiceGrpc.CreateScheduleServiceImplBase {

    private final ScoringCreationScheduleService scoringCreateScheduleService;

    @Override
    public void create(CreateScheduleRequest request, StreamObserver<CreateScheduleResponse> responseObserver) {
        log.info("Got request to schedule for scoring date: {}", request);

        responseObserver.onNext(
                scoringCreateScheduleService.createSchedule(request)
        );
        responseObserver.onCompleted();
    }
}
