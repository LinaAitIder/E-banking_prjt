package org.ebanking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BiometricAuthRequest(
        @NotBlank String email,
        @NotBlank String credentialId,
        @NotBlank String authenticatorData,
        @NotBlank String clientDataJSON,
        @NotBlank String signature
) {}

