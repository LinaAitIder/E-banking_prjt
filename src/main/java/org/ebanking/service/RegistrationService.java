package org.ebanking.service;

import org.ebanking.dao.UserRepository;
import org.ebanking.model.Client;
import org.ebanking.model.User;
import org.ebanking.model.WebAuthnCredential;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.WebAuthnCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistrationService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebAuthnCredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Client registerClient(Client client) {
        // Validation métier
        if (clientRepository.existsByNationalId(client.getNationalId())) {
            throw new IllegalStateException("National ID already registered");
        }

        // Enregistrement de base
        client.setWebAuthnEnabled(false);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }

    public void activateWebAuthn(User user, String credentialId, byte[] publicKey) {
        WebAuthnCredential credential = new WebAuthnCredential();
        credential.setUser(user);
        credential.setCredentialId(credentialId);
        credential.setPublicKey(publicKey);
        credential.setSignatureCount(0);

        credentialRepository.save(credential);

        user.setWebAuthnEnabled(true);
        userRepository.save(user);
    }

    public boolean phoneNumberExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}