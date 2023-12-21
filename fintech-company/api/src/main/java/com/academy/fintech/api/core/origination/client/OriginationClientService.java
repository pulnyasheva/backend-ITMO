package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.ApplicationRequest;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OriginationClientService {

    private final OriginationGrpcClient originationGrpcClient;

    public String createApplication(ApplicationDto applicationDto) {
        ApplicationRequest request = mapDtoToRequest(applicationDto);

        try {
            return originationGrpcClient.createApplication(request).getApplicationId();
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.ALREADY_EXISTS.getCode()) {
                return e.getTrailers().get(
                        Metadata.Key.of("applicationId", Metadata.ASCII_STRING_MARSHALLER));
            } else {
                throw e;
            }
        }
    }

    private static ApplicationRequest mapDtoToRequest(ApplicationDto applicationDto) {
        return ApplicationRequest.newBuilder()
                .setFirstName(applicationDto.firstName())
                .setLastName(applicationDto.lastName())
                .setEmail(applicationDto.email())
                .setSalary(applicationDto.salary())
                .setDisbursementAmount(applicationDto.amount())
                .build();
    }

}
