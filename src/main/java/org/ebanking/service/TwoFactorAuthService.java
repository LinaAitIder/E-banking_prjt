package org.ebanking.service;

import org.ebanking.dao.UserRepository;
import org.ebanking.dao.WebAuthnCredentialRepository;
import org.ebanking.model.Client;
import org.ebanking.model.CurrentAccount;
import org.ebanking.model.User;
import org.ebanking.model.enums.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TwoFactorAuthService {

    @Value("${infobip.base-url}")
    private String baseUrl;

    @Value("${infobip.api-key}")
    private String apiKey;

    @Value("${infobip.sender}")
    private String sender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Map<String, String> codeStorage = new HashMap<>();

    public void send2FACode(String phoneNumber) {
        String code = generateRandomCode();
        sendSms(phoneNumber, "Votre code de vérification E-Banking est : " + code);
        storeCode(phoneNumber, code);
    }

    public void sendSms(String phoneNumber, String messageText) {
        String url = baseUrl + "/sms/2/text/advanced";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "App " + apiKey);

        Map<String, Object> destination = Map.of("to", phoneNumber);
        Map<String, Object> message = Map.of(
                "from", "YourEBankingApp",
                "destinations", List.of(destination),
                "text", messageText
        );
        Map<String, Object> payload = Map.of("messages", List.of(message));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send SMS: " + response.getStatusCode());
        }

        System.out.println("Infobip Response Body: " + response.getBody());
    }

    public boolean validateCode(String phoneNumber, String code) {
        String stored = codeStorage.get(phoneNumber);
        System.out.println("Validating phone: " + phoneNumber + " with code: " + code + " (stored: " + stored + ")");
        return code != null && code.equals(stored);
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void storeCode(String phoneNumber, String code) {
        String convertedNumber=null;
        if (phoneNumber.startsWith("+212")) {
            convertedNumber = phoneNumber.replaceFirst("\\+212", "0");
        }else {
            convertedNumber = phoneNumber;
        }

        codeStorage.put(convertedNumber, code);
    }

    public String getUsernameByPhoneNumber(String phoneNumber) {
        String convertedNumber=null;
        if (phoneNumber.startsWith("+212")) {
            convertedNumber = phoneNumber.replaceFirst("\\+212", "0");
        }else {
            convertedNumber = phoneNumber;
        }
        return userRepository.findByPhone(convertedNumber).getFullName();
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        // Lookup user by phone number from DB or cache
        String convertedNumber= null;
        if (phoneNumber.startsWith("+212")) {
            convertedNumber = phoneNumber.replaceFirst("\\+212", "0");
        }else {
            convertedNumber = phoneNumber;
        }

        System.out.println(convertedNumber);
        return userRepository.findByPhone(convertedNumber);
    }

    public List<GrantedAuthority> getAuthoritiesByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhone(phoneNumber);
        return user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Client registerClient(Client client) {
        if (userRepository.existsByNationalId(client.getNationalId())) {
            throw new IllegalStateException("National ID already registered");
        }

        client.setWebAuthnEnabled(false);
        client.setPassword(passwordEncoder.encode(client.getPassword()));


        CurrentAccount defaultAccount = (CurrentAccount) accountFactory.createAccount(AccountType.CURRENT);
        client.addAccount(defaultAccount);

        return userRepository.save(client);
    }
}
