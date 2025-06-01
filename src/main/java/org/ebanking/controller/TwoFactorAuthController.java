package org.ebanking.controller;

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

    @PostMapping("/request-2fa")
    public ResponseEntity<?> request2FACode(@RequestParam String phoneNumber) {
        twoFactorService.send2FACode(phoneNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FA(
            @RequestParam String code,
            @RequestParam String phoneNumber,
            @RequestHeader("Authorization") String token) {

        if (twoFactorService.validateCode(phoneNumber, code)) {
            // Récupere les rôles existants depuis le token
            List<GrantedAuthority> authorities = jwtUtil.getGrantedAuthoritiesFromToken(token);

            // Regenère le token avec le même username et roles
            String newToken = jwtUtil.generateToken(
                    jwtUtil.extractUsername(token),
                    authorities
            );

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newToken)
                    .build();
        }
        return ResponseEntity.status(401).build();
    }
}

