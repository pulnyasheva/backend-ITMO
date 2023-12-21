package com.academy.fintech.origination.core.service.application;


import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.rejection.RejectRequest;
import com.academy.fintech.rejection.RejectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RejectApplicationService {
    private final ApplicationService applicationService;

    public RejectResponse rejectApplication(RejectRequest request) {
        return RejectResponse.newBuilder()
                .setIsReject(applicationService.updateStatus(request.getApplicationId(), ApplicationStatus.CLOSED))
                .build();
    }
}
