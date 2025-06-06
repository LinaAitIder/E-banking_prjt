package org.ebanking.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.ebanking.dao.UserRepository;
import org.ebanking.model.Client;
import org.ebanking.model.User;
import org.ebanking.service.GDPRService;
import org.ebanking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/gdpr")
public class GDPRController {

    @Autowired
    private GDPRService gdprService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/consent")
    public ResponseEntity<?> giveConsent(
            HttpServletRequest request,
            @RequestParam String consentType,
            @RequestParam(defaultValue = "P1Y") Duration validity) {

        User user = getCurrentUser(request);
        gdprService.recordConsent(user, consentType, validity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/consent")
    public ResponseEntity<?> revokeConsent(
            HttpServletRequest request,
            @RequestParam String consentType) {

        Long userId = getCurrentUserId(request);
        gdprService.revokeConsent(userId, consentType);
        return ResponseEntity.ok().build();
    }

    // Export des données (Client uniquement)
    @GetMapping("/export-client-data")
    public ResponseEntity<Resource> exportClientData(HttpServletRequest request) {
        try {
            // 1. Récupération de l'ID du client courant
            Long clientId = getCurrentUserId(request);

            // 2. Vérification que l'utilisateur est bien un client
            User currentUser = getCurrentUser(request);
            if (!(currentUser instanceof Client)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null);
            }

            // 3. Export des données
            Resource export = gdprService.exportClientData(clientId);

            // 4. Préparation de la réponse
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"client-data-" + clientId + ".zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(export);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }

    // Méthode pour récupérer l'utilisateur courant (Client ou Admin)
    private User getCurrentUser(HttpServletRequest request) {
        String token = jwtUtil.extractJwtFromRequest(request);
        if (token == null) {
            throw new AuthenticationCredentialsNotFoundException("No JWT token found");
        }
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Méthode pour récupérer l'ID de l'utilisateur courant
    private Long getCurrentUserId(HttpServletRequest request) {
        return getCurrentUser(request).getId();
    }

}
