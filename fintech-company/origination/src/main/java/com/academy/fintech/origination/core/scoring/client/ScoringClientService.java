package com.academy.fintech.origination.core.scoring.client;

import com.academy.fintech.check_scoring.CheckScoringRequest;
import com.academy.fintech.origination.core.scoring.client.grpc.ScoringGrpcClient;
import com.academy.fintech.origination.core.service.application.ScoringApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoringClientService {

    private final ScoringGrpcClient scoringGrpcClient;

    /**
     * Получение кредитного рейтинга клиента.
     */
    public int scoring(ScoringApplication application) {
        CheckScoringRequest request = mapApplicationToRequest(application);
        return scoringGrpcClient.scoring(request).getScore();
    }

    private static CheckScoringRequest mapApplicationToRequest(ScoringApplication application) {
        return CheckScoringRequest.newBuilder()
                .setClientId(application.clientId())
                .setClientSalary(application.clientSalary())
                .setDisbursementAmount(application.disbursementAmount())
                .setInterest(application.interest().toString())
                .setLoanTerm(application.loanTerm())
                .build();
    }
}
