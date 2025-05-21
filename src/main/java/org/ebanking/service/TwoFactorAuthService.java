package org.ebanking.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class TwoFactorAuthService {

    // Stockage temporaire des codes (remplacé par Redis en production)
    private static final Map<String, String> codeStorage = new HashMap<>();
    private static final long CODE_EXPIRATION_MINUTES = 5;

    @Value("${twilio.account-sid}")
    private String twilioSid;

    @Value("${twilio.auth-token}")
    private String twilioToken;

    @Value("${twilio.phone-number}")
    private String twilioNumber;

    public void send2FACode(String phoneNumber) {
        String code = generateRandomCode();
        Twilio.init(twilioSid, twilioToken);

        Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioNumber),
                "Votre code de vérification E-Banking est : " + code
        ).create();

        storeCode(phoneNumber, code);
    }

    public boolean validateCode(String phoneNumber, String code) {
        String storedCode = codeStorage.get(phoneNumber);
        return code != null && code.equals(storedCode);
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void storeCode(String phoneNumber, String code) {
        codeStorage.put(phoneNumber, code);
        // Nettoyage après expiration
        new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(CODE_EXPIRATION_MINUTES);
                codeStorage.remove(phoneNumber);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}