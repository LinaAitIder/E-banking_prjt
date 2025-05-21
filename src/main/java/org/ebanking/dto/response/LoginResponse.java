package org.ebanking.dto.response;

public class LoginResponse {
    private String token;
    private boolean webAuthnEnabled;

    public LoginResponse(String token, boolean webAuthnEnabled ) {
        this.token = token;
        this.webAuthnEnabled = webAuthnEnabled;
    }

    public String getToken() {
        return token;
    }

    public boolean isWebAuthnEnabled() {
        return webAuthnEnabled;
    }
}
