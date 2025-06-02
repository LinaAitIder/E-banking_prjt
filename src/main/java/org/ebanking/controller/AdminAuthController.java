package org.ebanking.controller;

import org.ebanking.dao.UserRepository;
import org.ebanking.dto.request.AdminLoginRequest;
import org.ebanking.dto.request.ChangePasswordRequest;
import org.ebanking.dto.request.NewPasswordRequest;
import org.ebanking.model.Admin;
import org.ebanking.util.AdminUserDetails;
import org.ebanking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminAuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/admin")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {
        try {
            // 1. Authentification
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 2. Récupération de l'admin
            Admin admin = userRepository.findAdminByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

            // 3. Vérification si premier login
            if (admin.isFirstLogin()) {
                Map<String, Object> response = new HashMap<>();
                response.put("requiresPasswordChange", true);
                response.put("temporaryToken", jwtUtil.generateTempToken(request.getEmail()));

                return ResponseEntity.status(HttpStatus.OK)
                        .body(response);
            }

            // 4. Génération du token normal
            List<GrantedAuthority> authorities = new ArrayList<>(
                    ((AdminUserDetails) authentication.getPrincipal()).getAuthorities()
            );

            String jwt = jwtUtil.generateToken(
                    request.getEmail(),
                    authorities
            );

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + jwt)
                    .build();

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }

    @PostMapping("/first-login/change-password")
    public ResponseEntity<?> firstLoginChangePassword(
            @RequestParam String tempToken,
            @RequestBody NewPasswordRequest request) {

        // 1. Validation du token temporaire
        if (!jwtUtil.validateTempToken(tempToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid or expired temporary token");
        }

        // 2. Récupération de l'admin
        String email = jwtUtil.extractUsername(tempToken);
        Admin admin = userRepository.findAdminByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        // 3. Mise à jour du mot de passe
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        admin.setFirstLogin(false); // Désactivation du flag
        userRepository.save(admin);

        // 4. Génération d'un vrai token JWT
        List<GrantedAuthority> authorities = admin.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String jwt = jwtUtil.generateToken(email, authorities);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordRequest request) {

        String email = jwtUtil.extractUsername(token.substring(7));
        Admin admin = userRepository.findAdminByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), admin.getPassword())) {
            return ResponseEntity.badRequest()
                    .body("Current password is incorrect");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(admin);

        return ResponseEntity.ok().build();
    }
}
