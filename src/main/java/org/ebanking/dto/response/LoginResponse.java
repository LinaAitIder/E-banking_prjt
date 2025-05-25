package org.ebanking.dto.response;

public class LoginResponse {
    private String token;
    private boolean webAuthnEnabled;
    private String userType;

    public LoginResponse(String token, boolean webAuthnEnabled, String userType ) {
        this.token = token;
        this.webAuthnEnabled = webAuthnEnabled;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public boolean isWebAuthnEnabled() {
        return webAuthnEnabled;
    }

    public String getUserType() { return userType; }
}
