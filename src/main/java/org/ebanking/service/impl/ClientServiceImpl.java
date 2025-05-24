package org.ebanking.service.impl;

import org.ebanking.dao.ClientRepository;
import org.ebanking.model.Client;
import org.ebanking.model.Account;
import org.ebanking.service.ClientService;
import org.ebanking.exception.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementation of the {@link ClientService} interface.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client createClient(Client client) {
        // Validate email uniqueness (example)
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + client.getEmail());
        }
        return clientRepository.save(client);
    }

    @Override
    public Client getClientById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + id));
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client updateClient(Integer id, Client clientDetails) {
        Client client = getClientById(id); // Throws ClientNotFoundException if not found
        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setEmail(clientDetails.getEmail());
        // Update other fields as needed
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Integer id) {
        Client client = getClientById(id); // Throws ClientNotFoundException if not found
        if (!client.getAccounts().isEmpty()) {
            throw new IllegalStateException("Cannot delete client with active accounts");
        }
        clientRepository.delete(client);
    }

    @Override
    public List<Account> getClientAccounts(Integer clientId) {
        Client client = getClientById(clientId);
        return client.getAccounts(); // Assumes a OneToMany relationship in the Client entity
    }
}