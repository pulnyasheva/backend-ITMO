package com.academy.fintech.pe;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.extension.Extension;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;



public class Container implements Extension {
    private static final Network network = Network.newNetwork();
    static int containerPort = 5432;
    static int localPort = 5433;
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.1-alpine")
            .withNetwork(network)
            .withNetworkAliases("postgres")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true)
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort),
                            new ExposedPort(containerPort)))
            ))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("postgres")));


    public static final ApplicationContainer applicationContainer = new ApplicationContainer()
            .withNetwork(network)
            .withNetworkAliases("app")
            .withEnv("DB_URL", "jdbc:postgresql://@postgres:5433/test-db")
            .withEnv("DB_PASSWORD", postgres.getPassword())
            .withEnv("DB_USERNAME", "test")
            .withEnv("SERVER_PORT", "8081")
            .withEnv("GRPC_PORT", "9091")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("app")));

    static {
        postgres.start();
        applicationContainer.start();
    }
}
