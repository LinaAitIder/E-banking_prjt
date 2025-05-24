package org.ebanking.service;

import org.ebanking.model.Client;
import org.ebanking.model.Account;
import java.util.List;


public interface ClientService {

    Client getClientById(Integer id);
    List<Client> getAllClients();
    Client updateClient(Integer id, Client clientDetails);
    void deleteClient(Integer id);

    List<Account> getClientAccounts(Integer clientId);
}