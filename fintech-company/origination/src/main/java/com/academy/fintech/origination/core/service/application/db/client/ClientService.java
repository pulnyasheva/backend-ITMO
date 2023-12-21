package com.academy.fintech.origination.core.service.application.db.client;

import com.academy.fintech.origination.core.service.application.Client;
import com.academy.fintech.origination.core.service.application.db.client.entity.EntityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public String save(Client client) {
        return clientRepository.save(mapClientToEntity(client)).getId();
    }

    /**
     * Создает клиента, если клиента с таким email еще нет или возвращает id уже существующего
     *
     * @return {@link String} id клиента
     */
    public String getOrCreate(Client client) {
        List<EntityClient> clients = clientRepository.findByEmail(client.email());
        if (clients.isEmpty()) {
            return save(client);
        }
        return clients.get(0).getId();
    }

    public Optional<Client> get(String clientId) {
        Optional<EntityClient> client = clientRepository.findById(clientId);
        return client.map(this::mapEntityToClient);
    }

    private EntityClient mapClientToEntity(Client client) {
        return EntityClient.builder()
                .firstName(client.firstName())
                .lastName(client.lastName())
                .email(client.email())
                .salary(client.salary())
                .build();
    }

    private Client mapEntityToClient(EntityClient entityClient) {
        return Client.builder()
                .id(entityClient.getId())
                .firstName(entityClient.getFirstName())
                .lastName(entityClient.getLastName())
                .email(entityClient.getEmail())
                .salary(entityClient.getSalary())
                .build();
    }
}
