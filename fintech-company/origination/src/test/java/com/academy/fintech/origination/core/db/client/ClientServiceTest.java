package com.academy.fintech.origination.core.db.client;

import com.academy.fintech.origination.core.db.client.entity.EntityClientTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ClientServiceTest {

    @Autowired
    private ClientRepositoryTest clientRepository;

    public Optional<String> getIdForEmail(String email) {
        List<EntityClientTest> clients = clientRepository.findByEmail(email);
        if (clients.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(clients.get(0).getId());
    }

    public Optional<EntityClientTest> get(String clientId) {
        return clientRepository.findById(clientId);
    }

    public String save(EntityClientTest client) {
        return clientRepository.save(client).getId();
    }
}
