package com.academy.fintech.origination.grpc;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.origination.Container;
import com.academy.fintech.origination.core.db.application.ApplicationServiceTest;
import com.academy.fintech.origination.core.db.application.entity.EntityApplicationTest;
import com.academy.fintech.origination.core.db.client.ClientServiceTest;
import com.academy.fintech.origination.core.db.client.entity.EntityClientTest;
import com.academy.fintech.origination.core.service.application.ApplicationStatus;
import com.academy.fintech.rejection.RejectRequest;
import com.academy.fintech.rejection.RejectResponse;
import com.academy.fintech.rejection.RejectServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.academy.fintech.origination.Container.applicationContainer;

@ExtendWith(Container.class)
@SpringBootTest
public class GrpcTest {

    private static ApplicationServiceGrpc.ApplicationServiceBlockingStub grpcServiceApplication;

    private static RejectServiceGrpc.RejectServiceBlockingStub grpcServiceReject;

    @Autowired
    private ApplicationServiceTest applicationService;

    @Autowired
    private ClientServiceTest clientService;


    @BeforeAll
    public static void AgreementManagementTest() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(applicationContainer.getHost(),
                applicationContainer.getGrpcPort()).usePlaintext().build();
        grpcServiceApplication = ApplicationServiceGrpc.newBlockingStub(channel);
        grpcServiceReject = RejectServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void createApplicationNewClient() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName("Ivan")
                .setLastName("Ivanov")
                .setEmail("ivanov@gmail.com")
                .setDisbursementAmount(55000)
                .setSalary(30000)
                .build();
        ApplicationResponse response = grpcServiceApplication.create(request);
        Assertions.assertNotNull(response);

        Optional<EntityApplicationTest> application = applicationService.getApplication(response.getApplicationId());
        Assertions.assertTrue(application.isPresent());

        Optional<String> clientId = clientService.getIdForEmail("ivanov@gmail.com");
        Assertions.assertTrue(clientId.isPresent());

        Optional<EntityClientTest> client = clientService.get(clientId.get());
        Assertions.assertTrue(client.isPresent());

        EntityClientTest expectedClient = EntityClientTest.builder()
                .id(clientId.get())
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivanov@gmail.com")
                .salary(30000)
                .build();
        Assertions.assertEquals(expectedClient, client.get());

        EntityApplicationTest expectedApplication = EntityApplicationTest.builder()
                .id(response.getApplicationId())
                .clientId(clientId.get())
                .requestedDisbursementAmount(55000)
                .status(ApplicationStatus.NEW)
                .build();
        Assertions.assertEquals(expectedApplication, application.get());
    }


    @Test
    public void createApplicationOldClient() {
        EntityClientTest client = EntityClientTest.builder()
                .id("client2")
                .firstName("Anton")
                .lastName("Ivanov")
                .email("ivanovAnton@gmail.com")
                .salary(30000)
                .build();
        String clientId = clientService.save(client);
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName("Anton")
                .setLastName("Ivanov")
                .setEmail("ivanovAnton@gmail.com")
                .setDisbursementAmount(55000)
                .setSalary(30000)
                .build();
        ApplicationResponse response = grpcServiceApplication.create(request);
        Assertions.assertNotNull(response);

        Optional<EntityApplicationTest> application = applicationService.getApplication(response.getApplicationId());
        Assertions.assertTrue(application.isPresent());

        EntityApplicationTest expectedApplication = EntityApplicationTest.builder()
                .id(response.getApplicationId())
                .clientId(clientId)
                .requestedDisbursementAmount(55000)
                .status(ApplicationStatus.NEW)
                .build();
        Assertions.assertEquals(expectedApplication, application.get());
    }


    @Test
    public void createDuplicateApplication() {
        EntityClientTest client = EntityClientTest.builder()
                .id("client3")
                .firstName("Anton")
                .lastName("Ivanov")
                .email("ivanovAnton123@gmail.com")
                .salary(30000)
                .build();
        clientService.save(client);
        ApplicationRequest request1 = ApplicationRequest.newBuilder()
                .setFirstName("Anton")
                .setLastName("Ivanov")
                .setEmail("ivanovAnton@gmail.com")
                .setDisbursementAmount(55000)
                .setSalary(30000)
                .build();
        ApplicationRequest request2 = ApplicationRequest.newBuilder()
                .setFirstName("Anton")
                .setLastName("Ivanov")
                .setEmail("ivanovAnton123@gmail.com")
                .setDisbursementAmount(55000)
                .setSalary(30000)
                .build();
        grpcServiceApplication.create(request1);
        Throwable thrown = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            grpcServiceApplication.create(request2);
        });

        Assertions.assertEquals("ALREADY_EXISTS: Duplicate application", thrown.getMessage());
    }

    @Test
    public void rejectExistApplication() {
        ApplicationRequest applicationRequest = ApplicationRequest.newBuilder()
                .setFirstName("Ivan")
                .setLastName("Ivanov")
                .setEmail("ivanovIvan@gmail.com")
                .setDisbursementAmount(55000)
                .setSalary(30000)
                .build();
        ApplicationResponse applicationResponse = grpcServiceApplication.create(applicationRequest);

        RejectRequest rejectRequest = RejectRequest.newBuilder()
                .setApplicationId(applicationResponse.getApplicationId())
                .build();

        RejectResponse rejectResponse = grpcServiceReject.reject(rejectRequest);
        Assertions.assertNotNull(rejectResponse);
        Assertions.assertTrue(rejectResponse.getIsReject());

        Optional<String> clientId = clientService.getIdForEmail("ivanovIvan@gmail.com");
        Assertions.assertTrue(clientId.isPresent());

        EntityApplicationTest expectedApplication = EntityApplicationTest.builder()
                .id(applicationResponse.getApplicationId())
                .clientId(clientId.get())
                .requestedDisbursementAmount(55000)
                .status(ApplicationStatus.CLOSED)
                .build();

        Optional<EntityApplicationTest> application = applicationService
                .getApplication(applicationResponse.getApplicationId());
        Assertions.assertTrue(application.isPresent());
        Assertions.assertEquals(expectedApplication, application.get());
    }

    @Test
    public void rejectNoExistApplication() {
        RejectRequest rejectRequest = RejectRequest.newBuilder()
                .setApplicationId("application")
                .build();

        RejectResponse rejectResponse = grpcServiceReject.reject(rejectRequest);
        Assertions.assertNotNull(rejectResponse);
        Assertions.assertFalse(rejectResponse.getIsReject());
    }

    @Test
    public void addExistClientEmail() {
        EntityClientTest client1 = EntityClientTest.builder()
                .id("client4")
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivanov@gmail.com")
                .salary(30000)
                .build();
        EntityClientTest client2 = EntityClientTest.builder()
                .id("client5")
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivanov@gmail.com")
                .salary(30000)
                .build();
        clientService.save(client1);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            clientService.save(client2);
        });
    }

}
