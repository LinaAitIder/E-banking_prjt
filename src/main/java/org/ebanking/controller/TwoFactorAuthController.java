package org.ebanking.controller;

import org.ebanking.dto.response.SmsVerifLoginResponse;
import org.ebanking.model.Client;
import org.ebanking.model.User;
import org.ebanking.service.UserService;
import org.ebanking.util.JwtUtil;
import org.ebanking.service.TwoFactorAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class TwoFactorAuthController {

    @Autowired
    private TwoFactorAuthService twoFactorService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/request-sms-verification")
    public ResponseEntity<?> request2FACode(@RequestParam("phoneNumber") String phoneNumber) {

            if (phoneNumber.startsWith("0")) {
                phoneNumber="+212" + phoneNumber.substring(1);
            }
            twoFactorService.send2FACode(phoneNumber);
            return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FA(
            @RequestParam("codeVerification") String code,
            @RequestParam("phoneNumber") String phoneNumber, @RequestBody Client client) {


        if (twoFactorService.validateCode(phoneNumber, code)) {
            twoFactorService.registerClient(client);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(401).build();
    }


    @PostMapping("/login-verify-2fa")
    public ResponseEntity<?> verify2FASMSLogin(
            @RequestParam("codeVerification") String code, @RequestParam("phoneNumber") String phoneNumber) {


        String standardizedNumber= null;
        if (phoneNumber.startsWith("+212")) {
            standardizedNumber = phoneNumber.replaceFirst("\\+212", "0");
        } else {
            standardizedNumber = phoneNumber;
        }

        if (twoFactorService.validateCode(standardizedNumber, code)) {
            User user = twoFactorService.getUserByPhoneNumber(standardizedNumber);
            String username = twoFactorService.getUsernameByPhoneNumber(standardizedNumber);
            String role = user.getRole();
            user.setWebAuthnEnabled(true);
            userService.enableWebAuthn(user);

            List<GrantedAuthority> authorities = twoFactorService.getAuthoritiesByPhoneNumber(standardizedNumber);
            String jwtToken = jwtUtil.generateToken(username, authorities);

            return ResponseEntity.ok()
                    .body(new SmsVerifLoginResponse(user, role, jwtToken));
        }

        return ResponseEntity.status(401).body("Invalid code");
    }

}

