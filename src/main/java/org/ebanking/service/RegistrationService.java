package org.ebanking.service;

import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.UserRepository;
import org.ebanking.model.*;
import org.ebanking.dao.WebAuthnCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.ebanking.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.ebanking.model.RibGenerator.generateRib;

@Service
@Transactional
public class RegistrationService {

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebAuthnCredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Client registerClient(Client client) {
        // Validation métier
        if (userRepository.existsByNationalId(client.getNationalId())) {
            throw new IllegalStateException("National ID already registered");
        }

        // Enregistrement de base
        client.setWebAuthnEnabled(false);
        client.setPassword(passwordEncoder.encode(client.getPassword()));


        // Création d'un compte courant par defaut
        CurrentAccount defaultAccount = (CurrentAccount) accountFactory.createAccount(AccountType.CURRENT);
        defaultAccount.setAccountNumber(generateAccountNumber());
        defaultAccount.setCurrency("MAD");
        defaultAccount.setOwner(client);
        defaultAccount.setRib(generateRib());
        client.addAccount(defaultAccount);
        client.setMainAccount(defaultAccount);

        userRepository.save(client);
        return client;
    }

    public BankAgent registerBankAgent(BankAgent agent) {
        // Validation métier
        if (userRepository.existsByEmail(agent.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }
        if (userRepository.existsByPhone(agent.getPhone())) {
            throw new IllegalStateException("Phone number already registered");
        }

        // Enregistrement de base
        agent.setWebAuthnEnabled(false);
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        return userRepository.save(agent);
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
        return userRepository.existsByPhone(phoneNumber);
    }
}