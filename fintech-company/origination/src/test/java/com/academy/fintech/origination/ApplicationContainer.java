package com.academy.fintech.origination;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

public class ApplicationContainer extends GenericContainer<ApplicationContainer> {
    private static final int GRPC_PORT = 9094;
    private static final int HEALTH_CHECK_PORT = 8084;

    public ApplicationContainer() {
        super(image());
    }

    @Override
    protected void configure() {
        super.configure();
        withExposedPorts(HEALTH_CHECK_PORT, GRPC_PORT);
    }

    private static Future<String> image() {
        Path dockerfilePath = Paths.get(System.getProperty("user.dir"), "Dockerfile");
        return new ImageFromDockerfile("test-app", true).withDockerfile(dockerfilePath);
    }

    public int getGrpcPort() {
        return this.getMappedPort(GRPC_PORT);
    }

}
