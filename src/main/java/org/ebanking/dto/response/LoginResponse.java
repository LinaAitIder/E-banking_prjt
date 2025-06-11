package org.ebanking.dto.response;

import org.ebanking.model.Client;
import org.ebanking.model.User;

import java.util.HashMap;
import java.util.Map;

public class LoginResponse {
    private String token;
    private boolean webAuthnEnabled;
    private String userType;
    private User user;

    public LoginResponse(String token, boolean webAuthnEnabled, String userType, User user ) {
        this.token = token;
        this.webAuthnEnabled = webAuthnEnabled;
        this.userType = userType;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public boolean isWebAuthnEnabled() {
        return webAuthnEnabled;
    }

    public String getUserType() { return userType; }

    public User getUser() { return user; }
}

