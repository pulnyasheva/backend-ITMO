package com.academy.fintech.origination.core.scoring.client.grpc;

import com.academy.fintech.check_scoring.CheckScoringRequest;
import com.academy.fintech.check_scoring.CheckScoringResponse;
import com.academy.fintech.check_scoring.CheckScoringServiceGrpc;
import com.academy.fintech.check_scoring.CheckScoringServiceGrpc.CheckScoringServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScoringGrpcClient {

    private final CheckScoringServiceBlockingStub stub;


    public ScoringGrpcClient(ScoringGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = CheckScoringServiceGrpc.newBlockingStub(channel);
    }

    public CheckScoringResponse scoring(CheckScoringRequest request) {
        try {
            return stub.scoring(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Scoring by request: {}", request, e);
            throw e;
        }
    }
}
