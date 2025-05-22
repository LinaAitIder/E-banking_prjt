package org.ebanking.service;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class PhoneVerificationService {
    @Value("${twilio.account-sid}")
    private String twilioSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Value("${twilio.phone-number}")
    private String twilioNumber;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public void sendVerificationCode(String phoneNumber) {
        String code = String.format("%06d", new Random().nextInt(999999));
        Twilio.init(twilioSid, twilioAuthToken);

        Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioNumber),
                "Votre code de vérification E-Banking : " + code
        ).create();

        verificationCodes.put(phoneNumber, code);
    }

    public boolean verifyCode(String phoneNumber, String code) {
        String storedCode = verificationCodes.get(phoneNumber);
        if (code.equals(storedCode)) {
            verificationCodes.remove(phoneNumber);
            return true;
        }
        return false;
    }
}
