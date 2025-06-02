package org.ebanking.dto.response;

import java.util.List;

public record AuthResponse(
        String challenge,
        List<String> allowedCredentials,
        String token,
        String role
) {}
