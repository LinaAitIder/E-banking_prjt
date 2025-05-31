package org.ebanking.service;

import org.ebanking.model.Client;
import org.ebanking.model.WebAuthnCredential;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.WebAuthnCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistrationService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WebAuthnCredentialRepository credentialRepository;

    public Client registerClient(Client client) {
        // Validation métier
        if (clientRepository.existsByNationalId(client.getNationalId())) {
            throw new IllegalStateException("National ID already registered");
        }

        // Enregistrement de base
        client.setWebAuthnEnabled(false);
        Client savedClient = clientRepository.save(client);

        return savedClient;
    }

    public void activateWebAuthn(Client client, String credentialId, byte[] publicKey) {
        WebAuthnCredential credential = new WebAuthnCredential();
        credential.setClient(client);
        credential.setCredentialId(credentialId);
        credential.setPublicKey(publicKey);
        credential.setSignatureCount(0);

        credentialRepository.save(credential);

        client.setWebAuthnEnabled(true);
        clientRepository.save(client);
    }

    public boolean phoneNumberExists(String phoneNumber) {
        return clientRepository.existsByPhoneNumber(phoneNumber);
    }
}